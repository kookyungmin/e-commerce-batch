package net.happykoo.ecb.batch.repository;

import java.time.LocalDate;
import net.happykoo.ecb.batch.domain.product.report.BrandReport;
import net.happykoo.ecb.batch.domain.product.report.BrandReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandReportRepository extends JpaRepository<BrandReport, BrandReportId> {

  Long countById_StatDate(LocalDate statDate);

}
