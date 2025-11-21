package net.happykoo.ecb.batch.domain.product.report;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryReportId implements Serializable {

  private LocalDate statDate = LocalDate.now();
  ;
  private String category;
}
