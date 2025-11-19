package net.happykoo.ecb.api.service.transaction;

import java.util.Optional;
import net.happykoo.ecb.api.domain.transaction.TransactionStatus;
import net.happykoo.ecb.api.domain.transaction.TransactionType;
import net.happykoo.ecb.api.service.order.dto.OrderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

  protected static Logger logger = LoggerFactory.getLogger(TransactionService.class);
  private final static String NA = "N/A";

  public void logTransaction(TransactionType transactionType, TransactionStatus transactionStatus,
      String message, OrderResult order) {
    try {
      putMdc(transactionType, transactionStatus, order);
      log(transactionStatus, message);
    } finally {
      //Mapped Diagnostic Context : 로그에 자동으로 붙는 label 같이 Context에 정보를 넣어두면, 로그마다 자동으로 붙어서 출력됨
      MDC.clear();
    }
  }

  private void putMdc(TransactionType transactionType, TransactionStatus transactionStatus,
      OrderResult order) {
    Optional.ofNullable(order)
        .ifPresentOrElse(this::putOrderInMdc, this::putNaOrderInMdc);
    putTransactionInMdc(transactionType, transactionStatus);
  }

  private void putTransactionInMdc(TransactionType transactionType,
      TransactionStatus transactionStatus) {
    MDC.put("transactionType", transactionType.toString());
    MDC.put("transactionStatus", transactionStatus.toString());
  }

  private void putOrderInMdc(OrderResult order) {
    MDC.put("orderId", order.orderId().toString());
    MDC.put("customerId", order.customerId().toString());
    MDC.put("totalAmount", order.totalAmount().toString());
    MDC.put("paymentMethod", order.paymentMethod().toString());
    MDC.put("productCount", order.productCount().toString());
    MDC.put("totalItemQuantity", order.totalItemQuantity().toString());
  }

  private void putNaOrderInMdc() {
    MDC.put("orderId", NA);
    MDC.put("customerId", NA);
    MDC.put("totalAmount", NA);
    MDC.put("paymentMethod", NA);
    MDC.put("productCount", NA);
    MDC.put("totalItemQuantity", NA);
  }

  private void log(TransactionStatus transactionStatus, String message) {
    if (transactionStatus == TransactionStatus.SUCCESS) {
      logger.info(message);
    } else {
      logger.error(message);
    }
  }
}
