package net.happykoo.ecb.api.service.product.dto;

import java.time.LocalDate;
import net.happykoo.ecb.api.domain.product.Product;
import net.happykoo.ecb.api.domain.product.ProductStatus;

public record ProductResult(
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

  public static ProductResult from(Product product) {
    return new ProductResult(
        product.getProductId(),
        product.getSellerId(),
        product.getCategory(),
        product.getProductName(),
        product.getSalesStartDate(),
        product.getSalesEndDate(),
        product.getProductStatus(),
        product.getBrand(),
        product.getManufacturer(),
        product.getSalesPrice(),
        product.getStockQuantity()
    );
  }
}
