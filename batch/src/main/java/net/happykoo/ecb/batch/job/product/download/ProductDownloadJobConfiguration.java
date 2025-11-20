package net.happykoo.ecb.batch.job.product.download;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import net.happykoo.ecb.batch.domain.file.PartitionedFileRepository;
import net.happykoo.ecb.batch.domain.product.Product;
import net.happykoo.ecb.batch.domain.product.ProductStatus;
import net.happykoo.ecb.batch.dto.product.ProductDownloadCsvRow;
import net.happykoo.ecb.batch.service.product.ProductDownloadPartitioner;
import net.happykoo.ecb.batch.util.FileUtils;
import net.happykoo.ecb.batch.util.ReflectionUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductDownloadJobConfiguration {

  @Bean
  public Job productDownloadJob(JobRepository jobRepository,
      Step productDownloadPartitionStep,
      Step productFileMergeStep,
      JobExecutionListener listener) {
    return new JobBuilder("productDownloadJob", jobRepository)
        .start(productDownloadPartitionStep)
        .next(productFileMergeStep)
        .listener(listener)
        .build();
  }

  @Bean
  public Step productDownloadPartitionStep(JobRepository jobRepository,
      Step productPagingStep,
      PartitionHandler productDownloadPartitionHandler,
      ProductDownloadPartitioner productDownloadPartitioner) {

    return new StepBuilder("productDownloadPartitionStep", jobRepository)
        .partitioner(productPagingStep.getName(), productDownloadPartitioner)
        .partitionHandler(productDownloadPartitionHandler)
        .allowStartIfComplete(true)
        .build();
  }

  @Bean
  @JobScope
  public TaskExecutorPartitionHandler productDownloadPartitionHandler(TaskExecutor taskExecutor,
      Step productPagingStep,
      @Value("#{jobParameters['gridSize']}") int gridSize) {
    TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();

    handler.setTaskExecutor(taskExecutor);
    handler.setStep(productPagingStep);
    handler.setGridSize(gridSize);

    return handler;
  }

  @Bean
  public Step productPagingStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      StepExecutionListener listener,
      ItemReader<Product> productPagingReader,
      ItemProcessor<Product, ProductDownloadCsvRow> productDownloadProcessor,
      ItemWriter<ProductDownloadCsvRow> productCsvWriter,
      TaskExecutor taskExecutor) {
    return new StepBuilder("productPagingStep", jobRepository)
        .<Product, ProductDownloadCsvRow>chunk(1000, transactionManager)
        .listener(listener)
        .reader(productPagingReader)
        .processor(productDownloadProcessor)
        .writer(productCsvWriter)
        .allowStartIfComplete(true)
        .taskExecutor(taskExecutor)
        .build();
  }

  @Bean
  @StepScope
  public JdbcPagingItemReader<Product> productPagingReader(DataSource dataSource,
      PagingQueryProvider pagingQueryProvider,
      @Value("#{stepExecutionContext['minId']}") String minId,
      @Value("#{stepExecutionContext['maxId']}") String maxId) {
    return new JdbcPagingItemReaderBuilder<Product>()
        .dataSource(dataSource)
        .name("productPagingReader")
        .queryProvider(pagingQueryProvider)
        .pageSize(1000)
        .rowMapper((rs, rowNum) -> Product.of(
            rs.getString("product_id"),
            rs.getLong("seller_id"),
            rs.getString("category"),
            rs.getString("product_name"),
            rs.getDate("sales_start_date").toLocalDate(),
            rs.getDate("sales_end_date").toLocalDate(),
            ProductStatus.valueOf(rs.getString("product_status")),
            rs.getString("brand"),
            rs.getString("manufacturer"),
            rs.getInt("sales_price"),
            rs.getInt("stock_quantity"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        ))
        .parameterValues(Map.of("minId", minId, "maxId", maxId))
        .build();
  }

  @Bean
  public SqlPagingQueryProviderFactoryBean productPagingQueryProvider(DataSource dataSource) {
    SqlPagingQueryProviderFactoryBean providerFactoryBean = new SqlPagingQueryProviderFactoryBean();

    providerFactoryBean.setDataSource(dataSource);
    providerFactoryBean.setSelectClause(
        "select product_id,seller_id,category,product_name,sales_start_date,sales_end_date,product_status,brand,manufacturer,sales_price,stock_quantity,created_at,updated_at"
    );
    providerFactoryBean.setFromClause("from products");
    providerFactoryBean.setWhereClause("product_id >= :minId and product_id <= :maxId");
    providerFactoryBean.setSortKey("product_id");

    return providerFactoryBean;
  }

  @Bean
  public ItemProcessor<Product, ProductDownloadCsvRow> productDownloadProcessor() {
    return ProductDownloadCsvRow::from;
  }

  @Bean
  @StepScope
  public SynchronizedItemStreamWriter<ProductDownloadCsvRow> productCsvWriter(
      @Value("#{stepExecutionContext['file']}") File file) {
    List<String> columns = ReflectionUtils.getFieldNames(ProductDownloadCsvRow.class);

    FlatFileItemWriter<ProductDownloadCsvRow> flatFileItemWriter = new FlatFileItemWriterBuilder<ProductDownloadCsvRow>()
        .name("productCsvWriter")
        .resource(new FileSystemResource(file))
        .delimited()
        .names(columns.toArray(String[]::new))
        .build();
    return new SynchronizedItemStreamWriterBuilder<ProductDownloadCsvRow>()
        .delegate(flatFileItemWriter)
        .build();
  }

  @Bean
  public Step productFileMergeStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      Tasklet productMergeTasklet,
      StepExecutionListener listener) {
    return new StepBuilder("productFileMergeStep", jobRepository)
        .tasklet(productMergeTasklet, transactionManager)
        .allowStartIfComplete(true)
        .listener(listener)
        .build();
  }

  @Bean
  @JobScope
  public Tasklet productFileMergeTasklet(
      @Value("#{jobParameters['outputFilePath']}") String filePath,
      PartitionedFileRepository repository) {
    return (contribution, chunkContext) -> {
      FileUtils.mergeFile(
          String.join(",", ReflectionUtils.getFieldNames(ProductDownloadCsvRow.class)),
          repository.getFiles(),
          new File(filePath)
      );
      return RepeatStatus.FINISHED;
    };
  }
}
