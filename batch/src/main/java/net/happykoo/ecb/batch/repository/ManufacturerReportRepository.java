package net.happykoo.ecb.batch.repository;

import java.time.LocalDate;
import net.happykoo.ecb.batch.domain.product.report.ManufacturerReport;
import net.happykoo.ecb.batch.domain.product.report.ManufacturerReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerReportRepository extends
    JpaRepository<ManufacturerReport, ManufacturerReportId> {

  Long countById_StatDate(LocalDate date);
}
