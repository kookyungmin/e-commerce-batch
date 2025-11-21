package net.happykoo.ecb.batch.repository;

import net.happykoo.ecb.batch.domain.transaction.TransactionReport;
import net.happykoo.ecb.batch.domain.transaction.TransactionReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionReportRepository extends
    JpaRepository<TransactionReport, TransactionReportId> {

}
