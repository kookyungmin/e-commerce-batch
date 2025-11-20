package net.happykoo.ecb.api.service.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import net.happykoo.ecb.api.domain.transaction.report.TransactionReport;

public record TransactionResult(
    LocalDate transactionDate,
    String transactionType,
    Long transactionCount,
    Long totalAmount,
    Long customerCount,
    Long orderCount,
    Long paymentMethodCount,
    BigDecimal avgProductCount,
    Long totalItemQuantity
) {

  public static TransactionResult from(TransactionReport transactionReport) {
    return new TransactionResult(
        transactionReport.getTransactionDate(),
        transactionReport.getTransactionType(),
        transactionReport.getTransactionCount(),
        transactionReport.getTotalAmount(),
        transactionReport.getCustomerCount(),
        transactionReport.getOrderCount(),
        transactionReport.getPaymentMethodCount(),
        transactionReport.getAvgProductCount(),
        transactionReport.getTotalItemQuantity()
    );
  }
}
