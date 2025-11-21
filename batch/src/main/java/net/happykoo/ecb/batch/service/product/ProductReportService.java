package net.happykoo.ecb.batch.service.product;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.batch.repository.BrandReportRepository;
import net.happykoo.ecb.batch.repository.CategoryReportRepository;
import net.happykoo.ecb.batch.repository.ManufacturerReportRepository;
import net.happykoo.ecb.batch.repository.ProductStatusReportRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReportService {

  private final BrandReportRepository brandReportRepository;
  private final CategoryReportRepository categoryReportRepository;
  private final ManufacturerReportRepository manufacturerReportRepository;
  private final ProductStatusReportRepository productStatusReportRepository;

  public Long brandReportCountByDate(LocalDate date) {
    return brandReportRepository.countById_StatDate(date);
  }

  public Long categoryReportCountByDate(LocalDate date) {
    return categoryReportRepository.countById_StatDate(date);
  }

  public Long manufacturerReportCountByDate(LocalDate date) {
    return manufacturerReportRepository.countById_StatDate(date);
  }

  public Long productStatusReportCountByDate(LocalDate date) {
    return productStatusReportRepository.countById_StatDate(date);
  }
}
