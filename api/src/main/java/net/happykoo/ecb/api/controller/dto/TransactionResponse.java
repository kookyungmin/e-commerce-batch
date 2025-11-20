package net.happykoo.ecb.api.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import net.happykoo.ecb.api.service.transaction.dto.TransactionResult;

public record TransactionResponse(
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

  public static TransactionResponse from(TransactionResult transactionResult) {
    return new TransactionResponse(
        transactionResult.transactionDate(),
        transactionResult.transactionType(),
        transactionResult.transactionCount(),
        transactionResult.totalAmount(),
        transactionResult.customerCount(),
        transactionResult.orderCount(),
        transactionResult.paymentMethodCount(),
        transactionResult.avgProductCount(),
        transactionResult.totalItemQuantity()
    );
  }
}
