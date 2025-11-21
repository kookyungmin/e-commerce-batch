package net.happykoo.ecb.batch.jobconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.happykoo.ecb.batch.BatchApplication;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBatchTest
@SpringJUnitConfig(classes = BatchApplication.class)
@AutoConfigureObservability
@ActiveProfiles("test")
public abstract class BaseBatchIntegrationTest {

  @Autowired
  protected JobLauncherTestUtils jobLauncherTestUtils;

  protected static void assertJobCompleted(JobExecution jobExecution) {
    assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getExitStatus().getExitCode());
  }
}
