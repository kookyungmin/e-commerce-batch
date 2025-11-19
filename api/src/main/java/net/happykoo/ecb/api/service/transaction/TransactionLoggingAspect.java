package net.happykoo.ecb.api.service.transaction;

import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.api.domain.transaction.TransactionStatus;
import net.happykoo.ecb.api.domain.transaction.TransactionType;
import net.happykoo.ecb.api.service.order.dto.OrderResult;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TransactionLoggingAspect {

  private final TransactionService transactionService;

  @Pointcut("execution(* net.happykoo.ecb.api.service.order.OrderService.createOrder(..))")
  public void createOrder() {
  }

  @AfterReturning(pointcut = "createOrder()", returning = "newOrder")
  public void logOrderCreationSuccess(Object newOrder) {
    transactionService.logTransaction(TransactionType.ORDER_CREATION, TransactionStatus.SUCCESS,
        "주문이 성공적으로 생성되었습니다. 결제 처리를 진행해주세요.", (OrderResult) newOrder);
  }

  @AfterThrowing(pointcut = "createOrder()", throwing = "exception")
  public void logOrderCreationFailure(Exception exception) {
    transactionService.logTransaction(TransactionType.ORDER_CREATION, TransactionStatus.FAILURE,
        "주문이 생성 중 에러 발생 : " + exception.getMessage(), null);
  }

  @Pointcut("execution(* net.happykoo.ecb.api.service.order.OrderService.completePayment(..))")
  public void completePayment() {
  }

  @AfterReturning(pointcut = "completePayment()", returning = "updatedOrder")
  public void logPaymentCompletionSuccess(Object updatedOrder) {
    if (((OrderResult) updatedOrder).paymentSuccess()) {
      transactionService.logTransaction(TransactionType.PAYMENT_COMPLETION,
          TransactionStatus.SUCCESS,
          "결제처리가 완료되었습니다.", (OrderResult) updatedOrder);
    } else {
      transactionService.logTransaction(TransactionType.PAYMENT_COMPLETION,
          TransactionStatus.FAILURE,
          "결제처리가 실패하였습니다.", (OrderResult) updatedOrder);
    }
  }

  @AfterThrowing(pointcut = "completePayment()", throwing = "exception")
  public void logPaymentCompletionFailure(Exception exception) {
    transactionService.logTransaction(TransactionType.PAYMENT_COMPLETION, TransactionStatus.FAILURE,
        "결제처리 중 에러 발생 : " + exception.getMessage(), null);
  }

  @Pointcut("execution(* net.happykoo.ecb.api.service.order.OrderService.completeOrder(..))")
  public void completeOrder() {
  }

  @AfterReturning(pointcut = "completeOrder()", returning = "completedOrder")
  public void logOrderCompletionSuccess(Object completedOrder) {
    transactionService.logTransaction(TransactionType.ORDER_COMPLETION,
        TransactionStatus.SUCCESS,
        "주문이 성공적으로 완료되었습니다.", (OrderResult) completedOrder);
  }

  @AfterThrowing(pointcut = "completeOrder()", throwing = "exception")
  public void logOrderCompletionFailure(Exception exception) {
    transactionService.logTransaction(TransactionType.ORDER_COMPLETION, TransactionStatus.FAILURE,
        "주문 완료 중 에러 발생 : " + exception.getMessage(), null);
  }

  @Pointcut("execution(* net.happykoo.ecb.api.service.order.OrderService.cancelOrder(..))")
  public void cancelOrder() {
  }

  @AfterReturning(pointcut = "cancelOrder()", returning = "canceledOrder")
  public void logOrderCancelSuccess(Object canceledOrder) {
    transactionService.logTransaction(TransactionType.ORDER_CANCEL,
        TransactionStatus.SUCCESS,
        "주문이 성공적으로 취소되었습니다.", (OrderResult) canceledOrder);
  }

  @AfterThrowing(pointcut = "cancelOrder()", throwing = "exception")
  public void logOrderCancelFailure(Exception exception) {
    transactionService.logTransaction(TransactionType.ORDER_CANCEL, TransactionStatus.FAILURE,
        "주문 취소 중 에러 발생 : " + exception.getMessage(), null);
  }
}
