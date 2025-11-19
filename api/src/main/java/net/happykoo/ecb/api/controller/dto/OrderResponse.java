package net.happykoo.ecb.api.controller.dto;

import java.time.LocalDateTime;
import java.util.List;
import net.happykoo.ecb.api.domain.order.OrderStatus;
import net.happykoo.ecb.api.domain.payment.PaymentMethod;
import net.happykoo.ecb.api.domain.payment.PaymentStatus;
import net.happykoo.ecb.api.service.order.dto.OrderResult;

public record OrderResponse(
    Long orderId,
    LocalDateTime orderDate,
    Long customerId,
    OrderStatus orderStatus,
    List<OrderItemResponse> orderItems,
    Long productCount,
    Long totalItemQuantity,
    Long paymentId,
    PaymentMethod paymentMethod,
    PaymentStatus paymentStatus,
    LocalDateTime paymentDate,
    Integer totalAmount,
    boolean paymentSuccess
) {

  public static OrderResponse from(OrderResult order) {
    return new OrderResponse(
        order.orderId(),
        order.orderDate(),
        order.customerId(),
        order.orderStatus(),
        order.orderItems()
            .stream()
            .map(OrderItemResponse::from)
            .toList(),
        order.productCount(),
        order.totalItemQuantity(),
        order.paymentId(),
        order.paymentMethod(),
        order.paymentStatus(),
        order.paymentDate(),
        order.totalAmount(),
        order.paymentSuccess()
    );
  }
}
