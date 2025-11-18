package net.happykoo.ecb.batch.job.product.download;

import java.util.List;
import javax.sql.DataSource;
import net.happykoo.ecb.batch.domain.product.Product;
import net.happykoo.ecb.batch.domain.product.ProductStatus;
import net.happykoo.ecb.batch.dto.ProductDownloadCsvRow;
import net.happykoo.ecb.batch.util.ReflectionUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
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
      Step productPagingStep,
      JobExecutionListener listener) {
    return new JobBuilder("productDownloadJob", jobRepository)
        .start(productPagingStep)
        .listener(listener)
        .build();
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
  public JdbcPagingItemReader<Product> productPagingReader(DataSource dataSource,
      PagingQueryProvider pagingQueryProvider) {
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
      @Value("#{jobParameters['outputFilePath']}") String path) {
    List<String> columns = ReflectionUtils.getFieldNames(ProductDownloadCsvRow.class);

    FlatFileItemWriter<ProductDownloadCsvRow> flatFileItemWriter = new FlatFileItemWriterBuilder<ProductDownloadCsvRow>()
        .name("productCsvWriter")
        .resource(new FileSystemResource(path))
        .delimited()
        .names(columns.toArray(String[]::new))
        .headerCallback(writer -> writer.write(String.join(",", columns)))
        .build();
    return new SynchronizedItemStreamWriterBuilder<ProductDownloadCsvRow>()
        .delegate(flatFileItemWriter)
        .build();
  }
}
