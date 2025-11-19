package net.happykoo.ecb.api.service.dto;

import net.happykoo.ecb.api.domain.order.OrderItem;

public record OrderItemResult(Long orderItemId, String productId, int quantity, int unitPrice) {

  public static OrderItemResult of(String productId, int quantity, int unitPrice) {
    return new OrderItemResult(null, productId, quantity, unitPrice);
  }

  public static OrderItemResult from(OrderItem orderItem) {
    return new OrderItemResult(
        orderItem.getOrderItemId(),
        orderItem.getProductId(),
        orderItem.getQuantity(),
        orderItem.getUnitPrice()
    );
  }
}
