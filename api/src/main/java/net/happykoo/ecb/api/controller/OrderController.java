package net.happykoo.ecb.api.controller;

import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.api.controller.dto.OrderRequest;
import net.happykoo.ecb.api.controller.dto.OrderResponse;
import net.happykoo.ecb.api.controller.dto.PaymentRequest;
import net.happykoo.ecb.api.service.OrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/orders")
@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
    return OrderResponse.from(
        orderService.createOrder(orderRequest.customerId(),
            orderRequest.getOrderItemCommands(),
            orderRequest.paymentMethod()));
  }

  @PostMapping("/{orderId}/payment")
  public OrderResponse completePayment(@PathVariable Long orderId,
      @RequestBody PaymentRequest paymentRequest) {
    return OrderResponse.from(orderService.completePayment(orderId, paymentRequest.success()));
  }

  @PostMapping("/{orderId}/complete")
  public OrderResponse completeOrder(@PathVariable Long orderId) {
    return OrderResponse.from(orderService.completeOrder(orderId));
  }

  @PostMapping("/{orderId}/cancel")
  public OrderResponse cancelOrder(@PathVariable Long orderId) {
    return OrderResponse.from(orderService.cancelOrder(orderId));
  }
}
