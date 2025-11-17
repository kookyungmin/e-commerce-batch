package net.happykoo.ecb.batch.service.monitoring;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BatchStepExecutionListener implements StepExecutionListener, ChunkListener {

  private final CustomPrometheusPushGatewayManager pushGatewayManager;

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    log.info("after step - execution context: {}", stepExecution.getExecutionContext());
    pushGatewayManager.pushMetrics(
        Map.of("job_name", stepExecution.getJobExecution().getJobInstance().getJobName()));

    return ExitStatus.COMPLETED;
  }

  @Override
  public void afterChunk(ChunkContext context) {
    pushGatewayManager.pushMetrics(
        Map.of("job_name",
            context.getStepContext().getStepExecution().getJobExecution().getJobInstance()
                .getJobName()));
    ChunkListener.super.afterChunk(context);
  }
}
