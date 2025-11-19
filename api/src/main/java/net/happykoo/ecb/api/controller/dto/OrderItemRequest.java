package net.happykoo.ecb.api.controller.dto;

import net.happykoo.ecb.api.service.order.dto.OrderItemCommand;

public record OrderItemRequest(String productId, int quantity) {
  public static OrderItemRequest of(String productId, int quantity) {
    return new OrderItemRequest(productId, quantity);
  }

  public OrderItemCommand toCommand() {
    return OrderItemCommand.of(productId, quantity);
  }
}
