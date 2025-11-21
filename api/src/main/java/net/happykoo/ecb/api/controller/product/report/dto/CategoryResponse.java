package net.happykoo.ecb.api.controller.product.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import net.happykoo.ecb.api.service.product.report.dto.CategoryResult;

public record CategoryResponse(
    LocalDate statDate,
    String category,
    Long productCount,
    BigDecimal avgSalesPrice,
    BigDecimal maxSalesPrice,
    BigDecimal minSalesPrice,
    Long totalStockQuantity,
    BigDecimal potentialSalesAmount
) {

  public static CategoryResponse from(CategoryResult categoryResult) {
    return new CategoryResponse(
        categoryResult.statDate(),
        categoryResult.category(),
        categoryResult.productCount(),
        categoryResult.avgSalesPrice(),
        categoryResult.maxSalesPrice(),
        categoryResult.minSalesPrice(),
        categoryResult.totalStockQuantity(),
        categoryResult.potentialSalesAmount()
    );
  }
}
