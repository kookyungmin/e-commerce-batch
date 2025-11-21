package net.happykoo.ecb.batch.domain.product.report;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BrandReportId implements Serializable {

  private LocalDate statDate;
  private String brand;
}
