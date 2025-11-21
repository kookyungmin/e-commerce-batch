package net.happykoo.ecb.batch.service.transaction;

import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.batch.repository.TransactionReportRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TransactionReportService {

  private final TransactionReportRepository transactionReportRepository;

  public Long countTransactionReports() {
    return transactionReportRepository.count();
  }

}
