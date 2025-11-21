package net.happykoo.ecb.api.controller.product.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import net.happykoo.ecb.api.service.product.report.dto.ProductStatusResult;

public record ProductStatusResponse(
    LocalDate statDate,
    String productStatus,
    Long productCount,
    BigDecimal avgStockQuantity

) {

  public static ProductStatusResponse from(ProductStatusResult productStatusResult) {
    return new ProductStatusResponse(
        productStatusResult.statDate(),
        productStatusResult.productStatus(),
        productStatusResult.productCount(),
        productStatusResult.avgStockQuantity()
    );
  }
}
