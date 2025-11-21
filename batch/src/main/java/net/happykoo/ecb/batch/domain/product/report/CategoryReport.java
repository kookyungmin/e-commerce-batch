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
@Table(name = "category_reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CategoryReport {

  @EmbeddedId
  private CategoryReportId id;

  private Long productCount;
  private Double avgSalesPrice;
  private Integer maxSalesPrice;
  private Integer minSalesPrice;
  private Long totalStockQuantity;
  private Double potentialSalesAmount;

  public CategoryReport(String category, Long productCount, Double avgSalesPrice,
      Integer maxSalesPrice, Integer minSalesPrice, Long totalStockQuantity,
      Double potentialSalesAmount) {
    this.id = new CategoryReportId(LocalDate.now(), category);
    this.productCount = productCount;
    this.avgSalesPrice = avgSalesPrice;
    this.maxSalesPrice = maxSalesPrice;
    this.minSalesPrice = minSalesPrice;
    this.totalStockQuantity = totalStockQuantity;
    this.potentialSalesAmount = potentialSalesAmount;
  }

}
