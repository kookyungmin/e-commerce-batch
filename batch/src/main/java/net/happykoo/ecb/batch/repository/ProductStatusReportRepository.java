package net.happykoo.ecb.batch.repository;

import java.time.LocalDate;
import net.happykoo.ecb.batch.domain.product.report.ProductStatusReport;
import net.happykoo.ecb.batch.domain.product.report.ProductStatusReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStatusReportRepository extends
    JpaRepository<ProductStatusReport, ProductStatusReportId> {

  Long countById_StatDate(LocalDate date);
}
