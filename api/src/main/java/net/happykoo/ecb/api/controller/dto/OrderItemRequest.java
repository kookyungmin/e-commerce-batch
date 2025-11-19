package net.happykoo.ecb.api.controller.dto;

import net.happykoo.ecb.api.service.dto.OrderItemCommand;

public record OrderItemRequest(String productId, int quantity) {

  public OrderItemCommand toCommand() {
    return OrderItemCommand.of(productId, quantity);
  }
}
