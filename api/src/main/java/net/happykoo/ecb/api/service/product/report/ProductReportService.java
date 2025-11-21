package net.happykoo.ecb.api.service.product.report;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.api.repository.BrandReportRepository;
import net.happykoo.ecb.api.repository.CategoryReportRepository;
import net.happykoo.ecb.api.repository.ManufacturerReportRepository;
import net.happykoo.ecb.api.repository.ProductStatusReportRepository;
import net.happykoo.ecb.api.service.product.report.dto.ProductReportResults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReportService {

  private final BrandReportRepository brandReportRepository;
  private final CategoryReportRepository categoryReportRepository;
  private final ManufacturerReportRepository manufacturerReportRepository;
  private final ProductStatusReportRepository productStatusReportRepository;

  public ProductReportResults findReports(LocalDate date) {
    return ProductReportResults.of(
        brandReportRepository.findAllById_StatDate(date),
        categoryReportRepository.findAllById_StatDate(date),
        manufacturerReportRepository.findAllById_StatDate(date),
        productStatusReportRepository.findAllById_StatDate(date)
    );
  }

}
