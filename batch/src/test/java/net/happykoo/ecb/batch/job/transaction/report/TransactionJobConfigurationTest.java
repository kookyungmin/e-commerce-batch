package net.happykoo.ecb.batch.job.transaction.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.IOException;
import net.happykoo.ecb.batch.jobconfig.BaseBatchIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "spring.batch.job.name=transactionReportJob")
class TransactionJobConfigurationTest extends BaseBatchIntegrationTest {

  @Value("classpath:/logs/transaction.log")
  private Resource resource;

  @Test
  void transactionReportJobTest(@Autowired Job transactionReportJob) throws Exception {
    jobLauncherTestUtils.setJob(transactionReportJob);
    JobParameters jobParameters = getJobParameters();

    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    assertAll(() -> assertJobCompleted(jobExecution),
        () -> assertThat(jdbcTemplate.queryForObject("select count(*) from transaction_reports",
            Integer.class)).isEqualTo(4));
  }

  private JobParameters getJobParameters() throws IOException {
    return new JobParametersBuilder()
        .addJobParameter("inputFilePath",
            new JobParameter<>(resource.getFile().getPath(), String.class, false))
        .addJobParameter("gridSize",
            new JobParameter<>(4, Integer.class, false))
        .toJobParameters();
  }
}