package net.happykoo.ecb.api.controller.product.report.dto;

import java.util.List;
import net.happykoo.ecb.api.service.product.report.dto.ProductReportResults;

public record ProductReportResponse(
    List<BrandResponse> brandResponses,
    List<CategoryResponse> categoryResponses,
    List<ManufacturerResponse> manufacturerResponses,
    List<ProductStatusResponse> productStatusResponses) {

  public static ProductReportResponse of(ProductReportResults productReportResults) {
    return new ProductReportResponse(
        productReportResults.brandReports().stream().map(BrandResponse::from).toList(),
        productReportResults.categoryResults().stream().map(CategoryResponse::from).toList(),
        productReportResults.manufacturerResults().stream().map(ManufacturerResponse::from).toList(),
        productReportResults.productStatusResults().stream().map(ProductStatusResponse::from).toList()
    );
  }
}
