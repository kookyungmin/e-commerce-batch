package net.happykoo.ecb.api.controller.product.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import net.happykoo.ecb.api.service.product.report.dto.ManufacturerResult;

public record ManufacturerResponse(
    LocalDate statDate,
    String manufacturer,
    Long productCount,
    BigDecimal avgSalesPrice,
    Long totalStockQuantity

) {

  public static ManufacturerResponse from(ManufacturerResult manufacturerResult) {
    return new ManufacturerResponse(
        manufacturerResult.statDate(),
        manufacturerResult.manufacturer(),
        manufacturerResult.productCount(),
        manufacturerResult.avgSalesPrice(),
        manufacturerResult.totalStockQuantity()
    );
  }
}
