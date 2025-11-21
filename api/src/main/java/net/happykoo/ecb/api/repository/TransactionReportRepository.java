package net.happykoo.ecb.api.repository;

import java.time.LocalDate;
import java.util.List;
import net.happykoo.ecb.api.domain.transaction.report.TransactionReport;
import net.happykoo.ecb.api.domain.transaction.report.TransactionReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionReportRepository extends
    JpaRepository<TransactionReport, TransactionReportId> {

  List<TransactionReport> findAllById_TransactionDate(LocalDate transactionDate);

}
