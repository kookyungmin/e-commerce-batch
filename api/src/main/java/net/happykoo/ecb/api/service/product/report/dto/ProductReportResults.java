package net.happykoo.ecb.api.service.product.report.dto;

import java.util.List;
import net.happykoo.ecb.api.domain.product.report.BrandReport;
import net.happykoo.ecb.api.domain.product.report.CategoryReport;
import net.happykoo.ecb.api.domain.product.report.ManufacturerReport;
import net.happykoo.ecb.api.domain.product.report.ProductStatusReport;

public record ProductReportResults(
    List<BrandResult> brandReports,
    List<CategoryResult> categoryResults,
    List<ManufacturerResult> manufacturerResults,
    List<ProductStatusResult> productStatusResults
) {

  public static ProductReportResults of(List<BrandReport> brandReports,
      List<CategoryReport> categoryReports,
      List<ManufacturerReport> manufacturerReports,
      List<ProductStatusReport> productStatusReports) {
    return new ProductReportResults(
      brandReports.stream().map(BrandResult::from).toList(),
      categoryReports.stream().map(CategoryResult::from).toList(),
      manufacturerReports.stream().map(ManufacturerResult::from).toList(),
      productStatusReports.stream().map(ProductStatusResult::from).toList()
    );
  }
}
