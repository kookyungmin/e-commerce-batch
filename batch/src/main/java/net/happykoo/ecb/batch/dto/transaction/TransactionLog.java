package net.happykoo.ecb.batch.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransactionLog(
    String timestamp,
    String level,
    String thread,
    TransactionMdc mdc,
    String logger,
    String message,
    String context
) {

  public String getTransactionType() {
    return mdc.transactionType();
  }

  public String getTransactionStatus() {
    return mdc.transactionStatus();
  }

  public Long getTotalAmount() {
    return convertNaValueIntoEmptyOptional(mdc.totalAmount())
        .map(Long::parseLong)
        .orElse(0L);
  }

  public Long getProductCount() {
    return convertNaValueIntoEmptyOptional(mdc.productCount())
        .map(Long::parseLong)
        .orElse(0L);
  }

  public Long getTotalItemQuantity() {
    return convertNaValueIntoEmptyOptional(mdc.totalItemQuantity())
        .map(Long::parseLong)
        .orElse(0L);
  }

  public String getPaymentMethod() {
    return convertNaValueIntoEmptyOptional(mdc.paymentMethod())
        .orElse(null);
  }

  public String getCustomerId() {
    return convertNaValueIntoEmptyOptional(mdc.customerId())
        .orElse(null);
  }

  public String getOrderId() {
    return convertNaValueIntoEmptyOptional(mdc.orderId())
        .orElse(null);
  }

  private Optional<String> convertNaValueIntoEmptyOptional(String value) {
    return value.equals("N/A") ? Optional.empty() : Optional.ofNullable(value);
  }
}
