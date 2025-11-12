package net.happykoo.ecb.batch.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductUploadCsvRow {

  private Long sellerId;

  private String category;
  private String productName;
  private String salesStartDate; //판매 시작일
  private String salesEndDate; //판매 종료일
  private String productStatus;
  private String brand; //브랜드
  private String manufacturer; //제조사

  private int salesPrice;

  public static ProductUploadCsvRow of(Long sellerId, String category, String productName,
      String salesStartDate, String salesEndDate, String productStatus, String brand,
      String manufacturer, int salesPrice, int stockQuantity) {
    return new ProductUploadCsvRow(
        sellerId,
        category,
        productName,
        salesStartDate,
        salesEndDate,
        productStatus,
        brand,
        manufacturer,
        salesPrice,
        stockQuantity
    );
  }

  private int stockQuantity;
}
