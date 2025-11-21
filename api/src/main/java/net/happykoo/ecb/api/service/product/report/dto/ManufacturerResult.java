package net.happykoo.ecb.api.service.product.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import net.happykoo.ecb.api.domain.product.report.ManufacturerReport;

public record ManufacturerResult(
  LocalDate statDate,
  String manufacturer,
  Long productCount,
  BigDecimal avgSalesPrice,
  Long totalStockQuantity
   
) {
  public static ManufacturerResult from(ManufacturerReport manufacturerReport) {
    return new ManufacturerResult(
      manufacturerReport.getId().getStatDate(),
      manufacturerReport.getId().getManufacturer(),
      manufacturerReport.getProductCount(),
      manufacturerReport.getAvgSalesPrice(),
      manufacturerReport.getTotalStockQuantity()
    );
  }
}
