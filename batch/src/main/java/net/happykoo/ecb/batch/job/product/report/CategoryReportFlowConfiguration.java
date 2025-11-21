package net.happykoo.ecb.batch.job.product.report;

import javax.sql.DataSource;
import net.happykoo.ecb.batch.domain.product.report.CategoryReport;
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
public class CategoryReportFlowConfiguration {

  @Bean
  public Flow categoryReportFlow(Step categoryReportStep) {
    return new FlowBuilder<SimpleFlow>("categoryReportFlow")
        .start(categoryReportStep)
        .build();
  }

  @Bean
  public Step categoryReportStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<CategoryReport> categoryReportReader,
      ItemWriter<CategoryReport> categoryReportWriter,
      StepExecutionListener listener) {
    return new StepBuilder("categoryReportStep", jobRepository)
        .<CategoryReport, CategoryReport>chunk(10, transactionManager)
        .reader(categoryReportReader)
        .writer(categoryReportWriter)
        .allowStartIfComplete(true)
        .listener(listener)
        .build();
  }

  @Bean
  public JdbcCursorItemReader<CategoryReport> categoryReportReader(DataSource dataSource) {
    var sql = """
        select category, 
          count(*) product_count,
          avg(sales_price) avg_sales_price, 
          min(sales_price) min_sales_price, 
          max(sales_price) max_sales_price, 
          sum(stock_quantity) total_stock_quantity,
          sum((sales_price * 1.0) * stock_quantity) potential_sales_amount
        from products
        group by category
        """;
    return new JdbcCursorItemReaderBuilder<CategoryReport>()
        .dataSource(dataSource)
        .name("categoryReportReader")
        .sql(sql)
        .beanRowMapper(CategoryReport.class)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<CategoryReport> categoryReportWriter(DataSource dataSource) {
    var sql = """
        insert into category_reports(stat_date, category, product_count, avg_sales_price, min_sales_price, max_sales_price, total_stock_quantity, potential_sales_amount)
        values (:statDate, :category, :productCount, :avgSalesPrice, :minSalesPrice, :maxSalesPrice, :totalStockQuantity, :potentialSalesAmount)
        """;
    return new JdbcBatchItemWriterBuilder<CategoryReport>()
        .dataSource(dataSource)
        .sql(sql)
        .beanMapped()
        .build();
  }

}
