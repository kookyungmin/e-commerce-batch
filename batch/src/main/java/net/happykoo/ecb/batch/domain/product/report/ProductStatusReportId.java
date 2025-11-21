package net.happykoo.ecb.batch.domain.product.report;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.happykoo.ecb.batch.domain.product.ProductStatus;

@Embeddable
@Getter
@AllArgsConstructor
public class ProductStatusReportId implements Serializable {

  private LocalDate statDate = LocalDate.now();
  @Enumerated(value = EnumType.STRING)
  private ProductStatus productStatus;
}
