package net.happykoo.ecb.api.repository;

import java.time.LocalDate;
import java.util.List;
import net.happykoo.ecb.api.domain.product.report.BrandReport;
import net.happykoo.ecb.api.domain.product.report.BrandReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandReportRepository extends JpaRepository<BrandReport, BrandReportId> {

  List<BrandReport> findAllById_StatDate(LocalDate statDate);
}
