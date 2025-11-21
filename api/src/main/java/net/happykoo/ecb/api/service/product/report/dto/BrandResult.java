package net.happykoo.ecb.api.service.product.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import net.happykoo.ecb.api.domain.product.report.BrandReport;

public record BrandResult(
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
  public static BrandResult from(BrandReport brandReport) {
    return new BrandResult(
      brandReport.getId().getStatDate(),
      brandReport.getId().getBrand(),
      brandReport.getProductCount(),
      brandReport.getAvgSalesPrice(),
      brandReport.getMinSalesPrice(),
      brandReport.getMaxSalesPrice(),
      brandReport.getTotalStockQuantity(),
      brandReport.getAvgStockQuantity(),
      brandReport.getTotalStockValue()
    );
  }   
}
