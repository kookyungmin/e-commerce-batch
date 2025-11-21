package net.happykoo.ecb.batch.job.product.report;

import javax.sql.DataSource;
import net.happykoo.ecb.batch.domain.product.report.ProductStatusReport;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductStatusFlowConfiguration {

  @Bean
  public Flow productStatusReportFlow(Step productStatusReportStep) {
    return new FlowBuilder<SimpleFlow>("productStatusReportFlow")
        .start(productStatusReportStep)
        .build();
  }

  @Bean
  public Step productStatusReportStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<ProductStatusReport> productStatusReportReader,
      ItemWriter<ProductStatusReport> productStatusReportWriter,
      StepExecutionListener listener) {
    return new StepBuilder("productStatusReportStep", jobRepository)
        .<ProductStatusReport, ProductStatusReport>chunk(10, transactionManager)
        .reader(productStatusReportReader)
        .writer(productStatusReportWriter)
        .allowStartIfComplete(true)
        .listener(listener)
        .build();
  }

  @Bean
  public JdbcCursorItemReader<ProductStatusReport> productStatusReportReader(
      DataSource dataSource) {
    var sql = """
        select product_status, 
          count(*) product_count,
          avg(stock_quantity) avg_stock_quantity
        from products
        group by product_status
        """;
    return new JdbcCursorItemReaderBuilder<ProductStatusReport>()
        .dataSource(dataSource)
        .name("productStatusReportReader")
        .sql(sql)
        .beanRowMapper(ProductStatusReport.class)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<ProductStatusReport> productStatusReportWriter(DataSource dataSource) {
    var sql = """
        insert into product_status_reports(stat_date, product_status, product_count, avg_stock_quantity)
        values (:statDate, :productStatus, :productCount, :avgStockQuantity)
        """;
    return new JdbcBatchItemWriterBuilder<ProductStatusReport>()
        .dataSource(dataSource)
        .sql(sql)
        .beanMapped()
        .build();
  }

}
