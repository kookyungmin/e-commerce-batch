package net.happykoo.ecb.batch.domain.product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.ecb.batch.dto.ProductUploadCsvRow;
import net.happykoo.ecb.batch.util.DateTimeUtils;
import net.happykoo.ecb.batch.util.RandomUtils;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Product {

  private String productId;
  private Long sellerId;

  private String category;
  private String productName;
  private LocalDate salesStartDate; //판매 시작일
  private LocalDate salesEndDate; //판매 종료일
  private ProductStatus productStatus;
  private String brand; //브랜드
  private String manufacturer; //제조사

  private int salesPrice;
  private int stockQuantity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static Product from(ProductUploadCsvRow row) {
    LocalDateTime now = LocalDateTime.now();
    return new Product(
        RandomUtils.getRandomId(),
        row.getSellerId(),
        row.getCategory(),
        row.getProductName(),
        DateTimeUtils.toLocalDate(row.getSalesStartDate()),
        DateTimeUtils.toLocalDate(row.getSalesStartDate()),
        ProductStatus.valueOf(row.getProductStatus()),
        row.getBrand(),
        row.getManufacturer(),
        row.getSalesPrice(),
        row.getStockQuantity(),
        now,
        now
    );
  }

  public static Product of(String productId, Long sellerId, String category, String productName,
      LocalDate salesStartDate, LocalDate salesEndDate, ProductStatus productStatus, String brand,
      String manufacturer, int salesPrice, int stockQuantity, LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    return new Product(productId, sellerId, category, productName, salesStartDate, salesEndDate,
        productStatus, brand, manufacturer, salesPrice, stockQuantity, createdAt, updatedAt);
  }
}
