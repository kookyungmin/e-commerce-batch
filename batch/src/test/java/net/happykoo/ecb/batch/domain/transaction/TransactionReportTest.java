package net.happykoo.ecb.batch.domain.transaction;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import net.happykoo.ecb.batch.util.DateTimeUtils;
import org.junit.jupiter.api.Test;

class TransactionReportTest {

  @Test
  void addTest() {
    TransactionReport report1 = createSampleTransactionReport("2025-11-20", "PURCHASE", 1L, 1000L,
        "HAPPYKOO-1", "ORDER-1", "CREDIT_CARD", 2L, 3L);
    TransactionReport report2 = createSampleTransactionReport("2025-11-20", "PURCHASE", 1L, 2000L,
        "HAPPYKOO-2", "ORDER-2", "DEBIT_CARD", 3L, 4L);

    report1.add(report2);

    assertAll(
        () -> assertEquals(2L, report1.getTransactionCount()),
        () -> assertEquals(3000L, report1.getTotalAmount()),
        () -> assertEquals(2L, report1.getCustomerCount()),
        () -> assertEquals(2L, report1.getOrderCount()),
        () -> assertEquals(2L, report1.getPaymentMethodCount()),
        () -> assertEquals(new BigDecimal("2.5"), report1.getAvgProductCount()),
        () -> assertEquals(7L, report1.getTotalItemQuantity())
    );
  }

  private TransactionReport createSampleTransactionReport(String date, String type, Long count,
      Long amount, String customerId, String orderId, String paymentMethod, Long productCount,
      Long itemQuantity) {
    return TransactionReport.of(
        DateTimeUtils.toLocalDate(date), type, count, amount, 1L, 1L, 1L,
        new BigDecimal(productCount), itemQuantity,
        new HashSet<>(List.of(customerId)),
        new HashSet<>(List.of(orderId)),
        new HashSet<>(List.of(paymentMethod)),
        productCount
    );
  }
}