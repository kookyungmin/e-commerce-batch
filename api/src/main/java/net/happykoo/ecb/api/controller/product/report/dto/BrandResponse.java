package net.happykoo.ecb.api.controller.product.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import net.happykoo.ecb.api.service.product.report.dto.BrandResult;

public record BrandResponse(
    LocalDate statDate,
    String brand,
    Long productCount,
    BigDecimal avgSalesPrice,
    BigDecimal minSalesPrice,
    BigDecimal maxSalesPrice,
    Long totalStockQuantity,
    BigDecimal avgStockQuantity,
    BigDecimal totalStockValue
) {

  public static BrandResponse from(BrandResult brandResult) {
    return new BrandResponse(
        brandResult.statDate(),
        brandResult.brand(),
        brandResult.productCount(),
        brandResult.avgSalesPrice(),
        brandResult.minSalesPrice(),
        brandResult.maxSalesPrice(),
        brandResult.totalStockQuantity(),
        brandResult.avgStockQuantity(),
        brandResult.totalStockValue()
    );
  }
}
