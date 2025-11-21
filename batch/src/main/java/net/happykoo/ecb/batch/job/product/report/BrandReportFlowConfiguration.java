package net.happykoo.ecb.batch.job.product.report;

import jakarta.persistence.EntityManagerFactory;
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
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
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
  public JpaCursorItemReader<BrandReport> brandReportReader(
      EntityManagerFactory entityManagerFactory) {
    var query = """
        select new BrandReport(p.brand, 
          count(p),
          avg(p.salesPrice), 
          max(p.salesPrice), 
          min(p.salesPrice), 
          sum(p.stockQuantity),
          avg(p.stockQuantity),
          sum(p.salesPrice * 1.0 * p.stockQuantity))
        from Product p
        group by p.brand
        """;
    return new JpaCursorItemReaderBuilder<BrandReport>()
        .entityManagerFactory(entityManagerFactory)
        .name("brandReportReader")
        .queryString(query)
        .build();
  }

  @Bean
  public JpaItemWriter<BrandReport> brandReportWriter(EntityManagerFactory entityManagerFactory) {
    return new JpaItemWriterBuilder<BrandReport>()
        .entityManagerFactory(entityManagerFactory)
        .usePersist(true)
        .build();
  }
}
