package net.happykoo.ecb.batch.domain.product.report;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Embeddable
@Getter
@AllArgsConstructor
public class ManufacturerReportId implements Serializable {

  private LocalDate statDate = LocalDate.now();
  private String manufacturer;
}
