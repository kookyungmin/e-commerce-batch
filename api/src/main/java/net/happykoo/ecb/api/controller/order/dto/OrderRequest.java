package net.happykoo.ecb.api.controller.order.dto;

import java.util.List;
import net.happykoo.ecb.api.domain.payment.PaymentMethod;
import net.happykoo.ecb.api.service.order.dto.OrderItemCommand;

public record OrderRequest(
    Long customerId,
    List<OrderItemRequest> orderItems,
    PaymentMethod paymentMethod
) {

  public static OrderRequest of(Long customerId, List<OrderItemRequest> orderItems,
      PaymentMethod paymentMethod) {
    return new OrderRequest(customerId, orderItems, paymentMethod);
  }

  public List<OrderItemCommand> toOrderItemCommands() {
    return orderItems.stream()
        .map(OrderItemRequest::toCommand)
        .toList();
  }
}
