package net.happykoo.ecb.api.service.product.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import net.happykoo.ecb.api.domain.product.report.ProductStatusReport;

public record ProductStatusResult(
  LocalDate statDate,
  String productStatus,
  Long productCount,
  BigDecimal avgStockQuantity

) {

  public static ProductStatusResult from(ProductStatusReport productStatusReport) {
    return new ProductStatusResult(
      productStatusReport.getId().getStatDate(),
      productStatusReport.getId().getProductStatus(),
      productStatusReport.getProductCount(),
      productStatusReport.getAvgStockQuantity()
    );
  }
}
