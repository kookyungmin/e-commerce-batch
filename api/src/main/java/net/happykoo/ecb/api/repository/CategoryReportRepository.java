package net.happykoo.ecb.api.repository;

import java.time.LocalDate;
import java.util.List;
import net.happykoo.ecb.api.domain.product.report.CategoryReport;
import net.happykoo.ecb.api.domain.product.report.CategoryReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryReportRepository extends JpaRepository<CategoryReport, CategoryReportId> {

  List<CategoryReport> findAllById_StatDate(LocalDate statDate);
}
