package net.happykoo.ecb.api.domain.product.report;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
public class ManufacturerReportId implements Serializable {

  private LocalDate statDate;
  private String manufacturer;
}
