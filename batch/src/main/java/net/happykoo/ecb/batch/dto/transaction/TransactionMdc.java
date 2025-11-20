package net.happykoo.ecb.batch.dto.transaction;

public record TransactionMdc(
    String transactionType,
    String totalAmount,
    String orderId,
    String transactionStatus,
    String customerId,
    String paymentMethod,
    String productCount,
    String totalItemQuantity
) {

}
