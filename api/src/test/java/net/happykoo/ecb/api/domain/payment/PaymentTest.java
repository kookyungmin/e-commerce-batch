package net.happykoo.ecb.api.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PaymentTest {

  private Payment payment;

  @BeforeEach
  void setup() {
    payment = Payment.createPayment(PaymentMethod.CREDIT_CARD, 1000, null);
  }

  @Test
  @DisplayName("payment 생성 테스트")
  void createPaymentTest() {
    assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING);
  }

  @Test
  @DisplayName("정상적으로 complete 되는 경우")
  void completeTest() {
    payment.complete();

    assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
  }

  @Test
  @DisplayName("complete 예외가 발생하는 경우")
  void completeExceptionTest() {
    payment.complete();

    assertThatThrownBy(payment::complete)
        .isInstanceOf(IllegalPaymentStateException.class);
  }

  @Test
  @DisplayName("정상적으로 fail 되는 경우")
  void failTest() {
    payment.fail();

    assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED);
  }

  @Test
  @DisplayName("fail 예외가 발생하는 경우")
  void failExceptionTest() {
    payment.complete();

    assertThatThrownBy(payment::fail)
        .isInstanceOf(IllegalPaymentStateException.class);
  }

  @Test
  @DisplayName("이미 결제완료 된 후에 정상적으로 cancel 되는 경우")
  void cancelAfterCompletedTest() {
    payment.complete();
    payment.cancel();

    assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED);
  }

  @Test
  @DisplayName("결제대기 상태에서 정상적으로 cancel 되는 경우")
  void cancelAfterPendingTest() {
    payment.cancel();

    assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CANCELED);
  }

  @Test
  @DisplayName("결제 실패된 후에 정상적으로 cancel 되는 경우")
  void cancelAfterFailedTest() {
    payment.fail();
    payment.cancel();

    assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CANCELED);
  }

  @Test
  @DisplayName("이미 취소된 경우 cancel 예외 발생")
  void cancelAfterCanceledTest() {
    payment.complete();
    payment.cancel();

    assertThatThrownBy(payment::cancel)
        .isInstanceOf(IllegalPaymentStateException.class);
  }

}