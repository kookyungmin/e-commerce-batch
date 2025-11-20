package net.happykoo.ecb.batch.job.transaction.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import net.happykoo.ecb.batch.domain.transaction.TransactionReport;
import net.happykoo.ecb.batch.domain.transaction.TransactionReportMapRepository;
import net.happykoo.ecb.batch.dto.transaction.TransactionLog;
import net.happykoo.ecb.batch.service.transaction.TransactionReportAccumulator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionJobConfiguration {

  @Bean
  public Job transactionReportJob(JobRepository jobRepository,
      JobExecutionListener jobExecutionListener,
      Step txAccumulatorStep,
      Step txSaveStep) {
    return new JobBuilder("transactionReportJob", jobRepository)
        .start(txAccumulatorStep)
        .next(txSaveStep)
        .listener(jobExecutionListener)
        .build();
  }

  @Bean
  public Step txAccumulatorStep(JobRepository jobRepository,
      StepExecutionListener stepExecutionListener,
      PlatformTransactionManager transactionManager,
      ItemReader<TransactionLog> logReader,
      ItemWriter<TransactionLog> logAccumulator,
      TaskExecutor taskExecutor) {
    return new StepBuilder("txAccumulatorStep", jobRepository)
        .<TransactionLog, TransactionLog>chunk(1000, transactionManager)
        .reader(logReader)
        .writer(logAccumulator)
        .allowStartIfComplete(true)
        .listener(stepExecutionListener)
        .taskExecutor(taskExecutor)
        .build();
  }

  @Bean
  @StepScope
  public SynchronizedItemStreamReader<TransactionLog> logReader(
      @Value("#{jobParameters['inputFilePath']}") String inputFilePath,
      ObjectMapper objectMapper) {
    FlatFileItemReader<TransactionLog> reader = new FlatFileItemReaderBuilder<TransactionLog>()
        .name("logReader")
        .resource(new FileSystemResource(inputFilePath))
        .lineMapper(((line, lineNumber) -> objectMapper.readValue(line, TransactionLog.class)))
        .build();
    return new SynchronizedItemStreamReaderBuilder<TransactionLog>()
        .delegate(reader)
        .build();
  }

  @Bean
  public ItemWriter<TransactionLog> logAccumulator(TransactionReportAccumulator accumulator) {
    return chunk -> {
      for (TransactionLog transactionLog : chunk.getItems()) {
        accumulator.accumulate(transactionLog);
      }
    };
  }

  @Bean
  public Step txSaveStep(JobRepository jobRepository,
      StepExecutionListener stepExecutionListener,
      PlatformTransactionManager transactionManager,
      ItemReader<TransactionReport> reportReader,
      ItemWriter<TransactionReport> reportWriter) {
    return new StepBuilder("txSaveStep", jobRepository)
        .<TransactionReport, TransactionReport>chunk(10, transactionManager)
        .reader(reportReader)
        .writer(reportWriter)
        .allowStartIfComplete(true)
        .listener(stepExecutionListener)
        .build();
  }

  @Bean
  @StepScope
  public ItemReader<TransactionReport> reportReader(
      TransactionReportMapRepository reportMapRepository) {
    return new IteratorItemReader<>(reportMapRepository.getTransactionReports());
  }

  @Bean
  @StepScope
  public JdbcBatchItemWriter<TransactionReport> reportWriter(DataSource dataSource) {
    var sql = """
            insert into transaction_reports(transaction_date, transaction_type, transaction_count, total_amount, customer_count, order_count, payment_method_count, avg_product_count, total_item_quantity)
            values (:transactionDate, :transactionType, :transactionCount, :totalAmount, :customerCount, :orderCount, :paymentMethodCount, :avgProductCount, :totalItemQuantity)
        """;
    return new JdbcBatchItemWriterBuilder<TransactionReport>()
        .dataSource(dataSource)
        .sql(sql)
        .beanMapped()
        .build();

  }

}
