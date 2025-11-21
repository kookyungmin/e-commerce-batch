package net.happykoo.ecb.api.repository;

import java.time.LocalDate;
import java.util.List;
import net.happykoo.ecb.api.domain.product.report.ProductStatusReport;
import net.happykoo.ecb.api.domain.product.report.ProductStatusReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStatusReportRepository extends
    JpaRepository<ProductStatusReport, ProductStatusReportId> {

  List<ProductStatusReport> findAllById_StatDate(LocalDate statDate);
}
