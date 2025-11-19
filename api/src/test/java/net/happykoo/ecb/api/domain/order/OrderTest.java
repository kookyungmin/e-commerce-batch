package net.happykoo.ecb.api.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import net.happykoo.ecb.api.domain.payment.PaymentMethod;
import net.happykoo.ecb.api.domain.payment.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {

  private Order order;

  @BeforeEach
  void setup() {
    order = Order.createOrder(1L);
    order.addOrderItem("P1", 2, 100);
    order.initPayment(PaymentMethod.CREDIT_CARD);
  }

  @Test
  void completePaymentTest() {
    order.completePayment();

    assertAll(
        () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING),
        () -> assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED),
        () -> assertThat(order.isPaymentSuccess()).isTrue()
    );
  }

  @Test
  void failPaymentTest() {
    order.failPayment();

    assertAll(
        () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PROCESSING),
        () -> assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.FAILED),
        () -> assertThat(order.isPaymentSuccess()).isFalse()
    );
  }

  @Test
  void completePaymentExceptionTest() {
    order.failPayment();
    assertThatThrownBy(() -> order.completePayment())
        .isInstanceOf(IllegalOrderStateException.class);
  }

  @Test
  void completeTest() {
    order.completePayment();
    order.complete();

    assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
  }

  @Test
  void completeAfterFailPaymentExceptionTest() {
    order.failPayment();

    assertThatThrownBy(() -> order.complete())
        .isInstanceOf(IllegalOrderStateException.class);
  }

  @Test
  void completeAfterCompletedExceptionTest() {
    order.completePayment();
    order.complete();
    assertThatThrownBy(() -> order.complete())
        .isInstanceOf(IllegalOrderStateException.class);
  }

  @Test
  void cancelTest() {
    order.completePayment();
    order.cancel();

    assertAll(
        () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELED),
        () -> assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.REFUNDED),
        () -> assertThat(order.isPaymentSuccess()).isFalse()
    );
  }

  @Test
  void cancelExceptionTest() {
    order.completePayment();
    order.complete();
    assertThatThrownBy(() -> order.cancel())
        .isInstanceOf(IllegalOrderStateException.class);
  }
}