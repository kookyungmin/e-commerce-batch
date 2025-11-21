package net.happykoo.ecb.api.service.transaction.report;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.api.repository.TransactionReportRepository;
import net.happykoo.ecb.api.service.transaction.dto.TransactionResults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionReportService {

  private final TransactionReportRepository repository;

  public TransactionResults findByDate(LocalDate date) {
    return TransactionResults.from(repository.findAllById_TransactionDate(date));
  }

}
