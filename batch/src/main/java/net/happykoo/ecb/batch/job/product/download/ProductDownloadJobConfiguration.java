package net.happykoo.ecb.batch.job.product.download;

import jakarta.persistence.EntityManagerFactory;
import java.io.File;
import java.util.List;
import java.util.Map;
import net.happykoo.ecb.batch.domain.file.PartitionedFileRepository;
import net.happykoo.ecb.batch.domain.product.Product;
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
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
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
  public JpaPagingItemReader<Product> productPagingReader(EntityManagerFactory entityManagerFactory,
      @Value("#{stepExecutionContext['minId']}") String minId,
      @Value("#{stepExecutionContext['maxId']}") String maxId) {
    var query = """
        select p from Product p where p.productId between :minId and :maxId order by p.productId
        """;
    return new JpaPagingItemReaderBuilder<Product>()
        .entityManagerFactory(entityManagerFactory)
        .name("productPagingReader")
        .queryString(query)
        .parameterValues(Map.of("minId", minId, "maxId", maxId))
        .pageSize(1000)
        .build();
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
