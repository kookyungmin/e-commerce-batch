package net.happykoo.ecb.batch.job.product.upload;

import java.io.File;
import java.sql.Timestamp;
import javax.sql.DataSource;
import net.happykoo.ecb.batch.domain.product.Product;
import net.happykoo.ecb.batch.dto.ProductUploadCsvRow;
import net.happykoo.ecb.batch.service.file.SplitFilePartitioner;
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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductUploadJobConfiguration {

  @Bean
  public Job productUploadJob(JobRepository jobRepository,
      Step productUploadPartitionStep,
      JobExecutionListener jobExecutionListener) {

    return new JobBuilder("productUploadJob", jobRepository)
        .listener(jobExecutionListener)
        .start(productUploadPartitionStep)
        .build();
  }

  @Bean
  public Step productUploadPartitionStep(JobRepository jobRepository,
      Step productUploadStep,
      PartitionHandler filePartitionHandler,
      SplitFilePartitioner splitFilePartitioner) {

    return new StepBuilder("productUploadPartitionStep", jobRepository)
        .partitioner(productUploadStep.getName(), splitFilePartitioner)
        .partitionHandler(filePartitionHandler)
        .allowStartIfComplete(true)
        .build();
  }

  @Bean
  @JobScope
  public SplitFilePartitioner splitFilePartitioner(
      @Value("#{jobParameters['inputFilePath']}") String path,
      @Value("#{jobParameters['gridSize']}") int gridSize) {
    return new SplitFilePartitioner(FileUtils.splitCsv(new File(path), gridSize));
  }

  @Bean
  @JobScope
  public TaskExecutorPartitionHandler filePartitionHandler(TaskExecutor taskExecutor,
      Step productUploadStep) {
    TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();

    handler.setTaskExecutor(taskExecutor);
    handler.setStep(productUploadStep);

    return handler;
  }

  @Bean
  public Step productUploadStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      StepExecutionListener stepExecutionListener,
      ItemReader<ProductUploadCsvRow> productReader,
      ItemProcessor<ProductUploadCsvRow, Product> productProcessor,
      ItemWriter<Product> productWriter,
      TaskExecutor taskExecutor) {

    return new StepBuilder("productUploadStep", jobRepository)
        .<ProductUploadCsvRow, Product>chunk(2000, transactionManager)
        .reader(productReader)
        .processor(productProcessor)
        .writer(productWriter)
        .allowStartIfComplete(true) //완료되어도 재실행 가능(개발단계에서만 true)
        .listener(stepExecutionListener)
        .taskExecutor(taskExecutor) //멀티 쓰레드 병렬 처리
        .build();
  }

  @Bean
  @StepScope
  public SynchronizedItemStreamReader<ProductUploadCsvRow> productReader(
      @Value("#{stepExecutionContext['file']}") String filePath) {
    FlatFileItemReader<ProductUploadCsvRow> fileItemReader = new FlatFileItemReaderBuilder<ProductUploadCsvRow>()
        .name("productReader")
        .resource(new FileSystemResource(filePath))
        .delimited() //default 콤마(,)
        .names(ReflectionUtils.getFieldName(ProductUploadCsvRow.class).toArray(String[]::new))
        .targetType(ProductUploadCsvRow.class)
//        .linesToSkip(1) //헤더는 제외할 때 사용 -> 파티셔닝에서 헤더 제외시킴
        .build();

    //Thread safe 하게 설정
    return new SynchronizedItemStreamReaderBuilder<ProductUploadCsvRow>()
        .delegate(fileItemReader)
        .build();
  }

  @Bean
  public ItemProcessor<ProductUploadCsvRow, Product> productProcessor() {
    return Product::from;
  }

  @Bean
  @StepScope
  public JdbcBatchItemWriter<Product> productWriter(DataSource dataSource) {
    String sql = """
        INSERT INTO products(product_id, seller_id, category, product_name, 
            sales_start_date, sales_end_date, product_status, brand, manufacturer, 
            sales_price, stock_quantity, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
//    VALUES (:productId, :sellerId, :category, :productName,
//                :salesStartDate, :salesEndDate, :productStatus, :brand, :manufacturer,
//                :salesPrice, :stockQuantity, :createdAt, :updatedAt)
    return new JdbcBatchItemWriterBuilder<Product>()
        .dataSource(dataSource)
        .sql(sql)
//        .beanMapped()
        .itemPreparedStatementSetter((product, ps) -> {
          ps.setString(1, product.getProductId());
          ps.setLong(2, product.getSellerId());
          ps.setString(3, product.getCategory());
          ps.setString(4, product.getProductName());
          ps.setObject(5, product.getSalesStartDate());
          ps.setObject(6, product.getSalesEndDate());
          ps.setString(7, product.getProductStatus().name());
          ps.setString(8, product.getBrand());
          ps.setString(9, product.getManufacturer());
          ps.setInt(10, product.getSalesPrice());
          ps.setInt(11, product.getStockQuantity());
          ps.setTimestamp(12, Timestamp.valueOf(product.getCreatedAt()));
          ps.setTimestamp(13, Timestamp.valueOf(product.getUpdatedAt()));
        })
        .build();
  }
}
