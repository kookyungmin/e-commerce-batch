package net.happykoo.ecb.batch.domain.product;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Product {

  private String productId;
  private Long sellerId;

  private String category;
  private String productName;
  private LocalDateTime salesStartDate; //판매 시작일
  private LocalDateTime salesEndDate; //판매 종료일
  private ProductStatus productStatus;
  private String brand; //브랜드
  private String manufacturer; //제조사

  private int salesPrice;
  private int stockQuantity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
