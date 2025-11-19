package net.happykoo.ecb.api.service.order.dto;

public record OrderItemCommand(String productId, int quantity) {

  public static OrderItemCommand of(String productId, int quantity) {
    return new OrderItemCommand(productId, quantity);
  }

}
