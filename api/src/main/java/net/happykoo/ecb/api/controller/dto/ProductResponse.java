package net.happykoo.ecb.api.controller.dto;

import java.time.LocalDate;
import net.happykoo.ecb.api.domain.product.ProductStatus;
import net.happykoo.ecb.api.service.dto.ProductResult;

public record ProductResponse(
    String productId,
    Long sellerId,
    String category,
    String productName,
    LocalDate salesStartDate,
    LocalDate salesEndDate,
    ProductStatus productStatus,
    String brand,
    String manufacturer,
    int salesPrice,
    int stockQuantity
) {

  public static ProductResponse from(ProductResult product) {
    return new ProductResponse(
        product.productId(),
        product.sellerId(),
        product.category(),
        product.productName(),
        product.salesStartDate(),
        product.salesEndDate(),
        product.productStatus(),
        product.brand(),
        product.manufacturer(),
        product.salesPrice(),
        product.stockQuantity()
    );
  }
}
