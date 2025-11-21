package net.happykoo.ecb.api.domain.product.report;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
  private BigDecimal avgSalesPrice;
  private Long totalStockQuantity;

}
