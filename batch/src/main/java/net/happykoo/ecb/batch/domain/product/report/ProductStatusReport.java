package net.happykoo.ecb.batch.domain.product.report;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.ecb.batch.domain.product.ProductStatus;

@Entity
@Table(name = "product_status_reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductStatusReport {

  @EmbeddedId
  private ProductStatusReportId id;

  private Long productCount;
  private Double avgStockQuantity;

  public ProductStatusReport(ProductStatus productStatus, Long productCount,
      Double avgStockQuantity) {
    this.id = new ProductStatusReportId(LocalDate.now(), productStatus);
    this.productCount = productCount;
    this.avgStockQuantity = avgStockQuantity;
  }
}
