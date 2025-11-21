package net.happykoo.ecb.api.repository;

import java.time.LocalDate;
import java.util.List;
import net.happykoo.ecb.api.domain.product.report.ManufacturerReport;
import net.happykoo.ecb.api.domain.product.report.ManufacturerReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerReportRepository extends
    JpaRepository<ManufacturerReport, ManufacturerReportId> {

  List<ManufacturerReport> findAllById_StatDate(LocalDate statDate);
}
