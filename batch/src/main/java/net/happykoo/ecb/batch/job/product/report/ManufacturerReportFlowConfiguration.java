package net.happykoo.ecb.batch.job.product.report;

import javax.sql.DataSource;
import net.happykoo.ecb.batch.domain.product.report.ManufacturerReport;
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
public class ManufacturerReportFlowConfiguration {

  @Bean
  public Flow manufacturerReportFlow(Step manufacturerReportStep) {
    return new FlowBuilder<SimpleFlow>("manufacturerReportFlow")
        .start(manufacturerReportStep)
        .build();
  }

  @Bean
  public Step manufacturerReportStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<ManufacturerReport> manufacturerReportReader,
      ItemWriter<ManufacturerReport> manufacturerReportWriter,
      StepExecutionListener listener) {
    return new StepBuilder("manufacturerReportStep", jobRepository)
        .<ManufacturerReport, ManufacturerReport>chunk(10, transactionManager)
        .reader(manufacturerReportReader)
        .writer(manufacturerReportWriter)
        .allowStartIfComplete(true)
        .listener(listener)
        .build();
  }

  @Bean
  public JdbcCursorItemReader<ManufacturerReport> manufacturerReportReader(DataSource dataSource) {
    var sql = """
        select manufacturer, 
          count(*) product_count,
          avg(sales_price) avg_sales_price, 
          sum(stock_quantity) total_stock_quantity
        from products
        group by manufacturer
        """;
    return new JdbcCursorItemReaderBuilder<ManufacturerReport>()
        .dataSource(dataSource)
        .name("manufacturerReportReader")
        .sql(sql)
        .beanRowMapper(ManufacturerReport.class)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<ManufacturerReport> manufacturerReportWriter(DataSource dataSource) {
    var sql = """
        insert into manufacturer_reports(stat_date, manufacturer, product_count, avg_sales_price, total_stock_quantity)
        values (:statDate, :manufacturer, :productCount, :avgSalesPrice, :totalStockQuantity)
        """;
    return new JdbcBatchItemWriterBuilder<ManufacturerReport>()
        .dataSource(dataSource)
        .sql(sql)
        .beanMapped()
        .build();
  }

}
