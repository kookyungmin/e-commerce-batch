package net.happykoo.ecb.batch.service.transaction;

import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.batch.domain.transaction.TransactionReport;
import net.happykoo.ecb.batch.domain.transaction.TransactionReportMapRepository;
import net.happykoo.ecb.batch.dto.transaction.TransactionLog;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionReportAccumulator {

  private final TransactionReportMapRepository repository;

  public void accumulate(TransactionLog transactionLog) {
    if (!"SUCCESS".equalsIgnoreCase(transactionLog.getTransactionStatus())) {
      return;
    }
    repository.put(TransactionReport.from(transactionLog));
  }
}
