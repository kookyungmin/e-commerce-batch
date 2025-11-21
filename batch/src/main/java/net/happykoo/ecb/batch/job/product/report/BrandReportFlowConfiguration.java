package net.happykoo.ecb.batch.job.product.report;

import javax.sql.DataSource;
import net.happykoo.ecb.batch.domain.product.report.BrandReport;
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
public class BrandReportFlowConfiguration {

  @Bean
  public Flow brandReportFlow(Step brandReportStep) {
    return new FlowBuilder<SimpleFlow>("brandReportFlow")
        .start(brandReportStep)
        .build();
  }

  @Bean
  public Step brandReportStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<BrandReport> brandReportReader,
      ItemWriter<BrandReport> brandReportWriter,
      StepExecutionListener listener) {
    return new StepBuilder("brandReportStep", jobRepository)
        .<BrandReport, BrandReport>chunk(10, transactionManager)
        .reader(brandReportReader)
        .writer(brandReportWriter)
        .allowStartIfComplete(true)
        .listener(listener)
        .build();
  }

  @Bean
  public JdbcCursorItemReader<BrandReport> brandReportReader(DataSource dataSource) {
    var sql = """
        select brand, 
          count(*) product_count,
          avg(sales_price) avg_sales_price, 
          min(sales_price) min_sales_price, 
          max(sales_price) max_sales_price, 
          sum(stock_quantity) total_stock_quantity,
          avg(stock_quantity) avg_stock_quantity,
          sum((sales_price * 1.0) * stock_quantity) total_stock_value
        from products
        group by brand
        """;
    return new JdbcCursorItemReaderBuilder<BrandReport>()
        .dataSource(dataSource)
        .name("brandReportReader")
        .sql(sql)
        .beanRowMapper(BrandReport.class)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<BrandReport> brandReportWriter(DataSource dataSource) {
    var sql = """
        insert into brand_reports(stat_date, brand, product_count, avg_sales_price, min_sales_price, max_sales_price, total_stock_quantity, avg_stock_quantity, total_stock_value)
        values (:statDate, :brand, :productCount, :avgSalesPrice, :minSalesPrice, :maxSalesPrice, :totalStockQuantity, :avgStockQuantity, :totalStockValue)
        """;
    return new JdbcBatchItemWriterBuilder<BrandReport>()
        .dataSource(dataSource)
        .sql(sql)
        .beanMapped()
        .build();
  }
}
