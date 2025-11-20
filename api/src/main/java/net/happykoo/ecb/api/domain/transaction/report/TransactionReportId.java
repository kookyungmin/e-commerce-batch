package net.happykoo.ecb.api.domain.transaction.report;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;

@Embeddable
@Getter
public class TransactionReportId implements Serializable {

  private LocalDate transactionDate;

  private String transactionType;

}
