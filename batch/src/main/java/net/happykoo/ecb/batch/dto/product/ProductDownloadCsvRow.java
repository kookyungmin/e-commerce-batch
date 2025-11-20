package net.happykoo.ecb.batch.dto.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.happykoo.ecb.batch.domain.product.Product;
import net.happykoo.ecb.batch.util.DateTimeUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDownloadCsvRow {

  private String productId;

  private Long sellerId;

  private String category;
  private String productName;
  private String salesStartDate; //판매 시작일
  private String salesEndDate; //판매 종료일
  private String productStatus;
  private String brand; //브랜드
  private String manufacturer; //제조사

  private int salesPrice;
  private int stockQuantity;
  private String createdAt;
  private String updatedAt;

  public static ProductDownloadCsvRow of(String productId, Long sellerId, String category,
      String productName,
      String salesStartDate, String salesEndDate, String productStatus, String brand,
      String manufacturer, int salesPrice, int stockQuantity, String createdAt, String updatedAt) {
    return new ProductDownloadCsvRow(
        productId,
        sellerId,
        category,
        productName,
        salesStartDate,
        salesEndDate,
        productStatus,
        brand,
        manufacturer,
        salesPrice,
        stockQuantity,
        createdAt,
        updatedAt);
  }

  public static ProductDownloadCsvRow from(Product product) {
    return new ProductDownloadCsvRow(
        product.getProductId(),
        product.getSellerId(),
        product.getCategory(),
        product.getProductName(),
        DateTimeUtils.toString(product.getSalesStartDate()),
        DateTimeUtils.toString(product.getSalesEndDate()),
        product.getProductStatus().name(),
        product.getBrand(),
        product.getManufacturer(),
        product.getSalesPrice(),
        product.getStockQuantity(),
        DateTimeUtils.toString(product.getCreatedAt()),
        DateTimeUtils.toString(product.getUpdatedAt()));
  }
}
