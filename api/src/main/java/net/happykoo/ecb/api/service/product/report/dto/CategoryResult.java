package net.happykoo.ecb.api.service.product.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import net.happykoo.ecb.api.domain.product.report.CategoryReport;

public record CategoryResult(
  LocalDate statDate,
  String category,
  Long productCount,
  BigDecimal avgSalesPrice,
  BigDecimal maxSalesPrice,
  BigDecimal minSalesPrice,
  Long totalStockQuantity,
  BigDecimal potentialSalesAmount
) {
  public static CategoryResult from(CategoryReport categoryReport) {
    return new CategoryResult(
      categoryReport.getId().getStatDate(),
      categoryReport.getId().getCategory(),
      categoryReport.getProductCount(),
      categoryReport.getAvgSalesPrice(),
      categoryReport.getMaxSalesPrice(),
      categoryReport.getMinSalesPrice(),
      categoryReport.getTotalStockQuantity(),
      categoryReport.getPotentialSalesAmount()
    );
  }
}
