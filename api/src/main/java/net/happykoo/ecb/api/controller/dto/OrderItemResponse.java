package net.happykoo.ecb.api.controller.dto;

import net.happykoo.ecb.api.service.dto.OrderItemResult;

public record OrderItemResponse(Long orderItemId, String productId, int quantity, int unitPrice) {

  public static OrderItemResponse of(String productId, int quantity, int unitPrice) {
    return new OrderItemResponse(null, productId, quantity, unitPrice);
  }

  public static OrderItemResponse from(OrderItemResult orderItem) {
    return new OrderItemResponse(
        orderItem.orderItemId(),
        orderItem.productId(),
        orderItem.quantity(),
        orderItem.unitPrice()
    );
  }
}
