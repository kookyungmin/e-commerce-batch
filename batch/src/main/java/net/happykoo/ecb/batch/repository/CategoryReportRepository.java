package net.happykoo.ecb.batch.repository;

import java.time.LocalDate;
import net.happykoo.ecb.batch.domain.product.report.CategoryReport;
import net.happykoo.ecb.batch.domain.product.report.CategoryReportId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryReportRepository extends JpaRepository<CategoryReport, CategoryReportId> {

  Long countById_StatDate(LocalDate date);
}
