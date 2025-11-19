package net.happykoo.ecb.api.controller.dto;

import java.util.List;
import net.happykoo.ecb.api.domain.payment.PaymentMethod;
import net.happykoo.ecb.api.service.order.dto.OrderItemCommand;

public record OrderRequest(
    Long customerId,
    List<OrderItemRequest> orderItems,
    PaymentMethod paymentMethod
) {

  public List<OrderItemCommand> getOrderItemCommands() {
    return orderItems.stream()
        .map(OrderItemRequest::toCommand)
        .toList();
  }
}
