package net.happykoo.ecb.api.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Product {

  @Id
  private String productId;
  private Long sellerId;

  private String category;
  private String productName;
  private LocalDate salesStartDate; //판매 시작일
  private LocalDate salesEndDate; //판매 종료일
  @Enumerated(value = EnumType.STRING)
  private ProductStatus productStatus;
  private String brand; //브랜드
  private String manufacturer; //제조사

  private int salesPrice;
  private int stockQuantity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static Product of(String productId, Long sellerId, String category, String productName,
      LocalDate salesStartDate, LocalDate salesEndDate, ProductStatus productStatus, String brand,
      String manufacturer, int salesPrice, int stockQuantity, LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    return new Product(productId, sellerId, category, productName, salesStartDate, salesEndDate,
        productStatus, brand, manufacturer, salesPrice, stockQuantity, createdAt, updatedAt);
  }

  public void increaseStock(int stockQuantity) {
    if (stockQuantity <= 0 || this.stockQuantity + stockQuantity < 0) {
      throw new StockQuantityArithmeticException();
    }
    this.stockQuantity = this.stockQuantity + stockQuantity;
  }

  public void decreaseStock(int stockQuantity) {
    if (stockQuantity <= 0 || this.stockQuantity - stockQuantity < 0) {
      throw new StockQuantityArithmeticException();
    }
    this.stockQuantity = this.stockQuantity - stockQuantity;
  }
}
