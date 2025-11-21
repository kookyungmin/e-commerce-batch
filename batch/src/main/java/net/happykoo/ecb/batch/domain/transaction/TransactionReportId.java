package net.happykoo.ecb.batch.domain.transaction;

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
public class TransactionReportId implements Serializable {

  private LocalDate transactionDate;

  private String transactionType;

}
