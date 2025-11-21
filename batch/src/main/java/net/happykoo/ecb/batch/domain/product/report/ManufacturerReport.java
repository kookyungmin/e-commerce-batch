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
@Table(name = "manufacturer_reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ManufacturerReport {

  @EmbeddedId
  private ManufacturerReportId id;

  private Long productCount;
  private Double avgSalesPrice;
  private Long totalStockQuantity;

  public ManufacturerReport(String manufacturer, Long productCount, Double avgSalesPrice,
      Long totalStockQuantity) {
    this.id = new ManufacturerReportId(LocalDate.now(), manufacturer);
    this.productCount = productCount;
    this.avgSalesPrice = avgSalesPrice;
    this.totalStockQuantity = totalStockQuantity;
  }

}
