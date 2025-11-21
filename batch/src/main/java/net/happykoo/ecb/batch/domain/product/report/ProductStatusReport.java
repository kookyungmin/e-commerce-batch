package net.happykoo.ecb.batch.domain.product.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProductStatusReport {

  private LocalDate statDate = LocalDate.now();
  private String productStatus;

  private Long productCount;
  private BigDecimal avgStockQuantity;
}
