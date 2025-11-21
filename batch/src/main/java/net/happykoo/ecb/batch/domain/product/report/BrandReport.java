package net.happykoo.ecb.batch.domain.product.report;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "brand_reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BrandReport {

  @EmbeddedId
  private BrandReportId id;

  private Long productCount;
  private Double avgSalesPrice;
  private Integer maxSalesPrice;
  private Integer minSalesPrice;
  private Long totalStockQuantity;
  private Double avgStockQuantity;
  private Double totalStockValue; //재고 가치

  public BrandReport(String brand,
      Long productCount,
      Double avgSalesPrice,
      Integer maxSalesPrice,
      Integer minSalesPrice,
      Long totalStockQuantity,
      Double avgStockQuantity,
      Double totalStockValue) {
    this(new BrandReportId(LocalDate.now(), brand), productCount, avgSalesPrice, maxSalesPrice,
        minSalesPrice, totalStockQuantity, avgStockQuantity, totalStockValue);
  }
}
