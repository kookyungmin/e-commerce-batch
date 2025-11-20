package net.happykoo.ecb.batch.domain.transaction;

import jakarta.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.ecb.batch.dto.transaction.TransactionLog;
import net.happykoo.ecb.batch.util.DateTimeUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionReport {

  private LocalDate transactionDate;

  private String transactionType;

  private Long transactionCount;

  private Long totalAmount;

  private Long customerCount;

  private Long orderCount;

  private Long paymentMethodCount;

  private BigDecimal avgProductCount;

  private Long totalItemQuantity;

  @Transient
  private Set<String> customerSet;

  @Transient
  private Set<String> orderSet;

  @Transient
  private Set<String> paymentMethodSet;

  @Transient
  private Long sumProductCount;

  public static TransactionReport of(LocalDate transactionDate, String transactionType,
      Long transactionCount, Long totalAmount, Long customerCount, Long orderCount,
      Long paymentMethodCount, BigDecimal avgProductCount, Long totalItemQuantity,
      Set<String> customerSet, Set<String> orderSet, Set<String> paymentMethodSet,
      Long sumProductCount) {
    return new TransactionReport(transactionDate, transactionType, transactionCount, totalAmount,
        customerCount, orderCount, paymentMethodCount, avgProductCount, totalItemQuantity,
        customerSet, orderSet, paymentMethodSet, sumProductCount);
  }

  public static TransactionReport from(TransactionLog log) {
    return new TransactionReport(
        DateTimeUtils.toLocalDateTime(log.timestamp()).toLocalDate(),
        log.getTransactionType(),
        1L,
        log.getTotalAmount(),
        1L,
        1L,
        1L,
        new BigDecimal(log.getProductCount()),
        log.getTotalItemQuantity(),
        new HashSet<>(List.of(log.getCustomerId())),
        new HashSet<>(List.of(log.getOrderId())),
        new HashSet<>(List.of(log.getPaymentMethod())),
        log.getProductCount()
    );
  }

  public void add(TransactionReport report) {
    customerSet.addAll(report.getCustomerSet());
    orderSet.addAll(report.getOrderSet());
    paymentMethodSet.addAll(report.getPaymentMethodSet());
    sumProductCount += report.getSumProductCount();

    transactionCount += report.getTransactionCount();
    totalAmount += report.getTotalAmount();
    customerCount = (long) customerSet.size();
    orderCount = (long) orderSet.size();
    paymentMethodCount = (long) paymentMethodSet.size();
    avgProductCount = new BigDecimal((double) sumProductCount / transactionCount);
    totalItemQuantity += report.getTotalItemQuantity();
  }
}
