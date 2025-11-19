package net.happykoo.ecb.api.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import net.happykoo.ecb.api.domain.order.Order;
import net.happykoo.ecb.api.domain.order.OrderStatus;
import net.happykoo.ecb.api.domain.payment.PaymentMethod;
import net.happykoo.ecb.api.domain.payment.PaymentStatus;

public record OrderResult(
    Long orderId,
    LocalDateTime orderDate,
    Long customerId,
    OrderStatus orderStatus,
    List<OrderItemResult> orderItems,
    Long productCount,
    Long totalItemQuantity,
    Long paymentId,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    LocalDateTime paymentDate,
    Integer totalAmount,
    boolean paymentSuccess
) {

  public static OrderResult from(Order order) {
    return new OrderResult(
        order.getOrderId(),
        order.getOrderDate(),
        order.getCustomerId(),
        order.getOrderStatus(),
        order.getOrderItems()
            .stream()
            .map(OrderItemResult::from)
            .toList(),
        order.countProducts(),
        order.calculateTotalItemQuantity(),
        order.getPaymentId(),
        order.getPaymentMethod(),
        order.getPaymentStatus(),
        order.getPaymentDate(),
        order.calculateTotalAmount(),
        order.isPaymentSuccess()
    );
  }
}
