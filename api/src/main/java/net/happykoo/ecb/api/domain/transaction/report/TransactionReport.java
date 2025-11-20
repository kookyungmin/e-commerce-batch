package net.happykoo.ecb.api.domain.transaction.report;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_reports")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TransactionReport implements Serializable {

  @EmbeddedId
  private TransactionReportId id;

  private Long transactionCount;
  private Long totalAmount;
  private Long customerCount;
  private Long orderCount;
  private Long paymentMethodCount;
  private BigDecimal avgProductCount;
  private Long totalItemQuantity;

  public LocalDate getTransactionDate() {
    return id.getTransactionDate();
  }

  public String getTransactionType() {
    return id.getTransactionType();
  }
}
