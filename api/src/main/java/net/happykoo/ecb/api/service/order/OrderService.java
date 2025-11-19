package net.happykoo.ecb.api.service.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.api.domain.order.Order;
import net.happykoo.ecb.api.domain.order.OrderItem;
import net.happykoo.ecb.api.domain.order.OrderNotFoundException;
import net.happykoo.ecb.api.domain.payment.PaymentMethod;
import net.happykoo.ecb.api.repository.OrderRepository;
import net.happykoo.ecb.api.service.order.dto.OrderItemCommand;
import net.happykoo.ecb.api.service.order.dto.OrderResult;
import net.happykoo.ecb.api.service.product.ProductService;
import net.happykoo.ecb.api.service.product.dto.ProductResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final ProductService productService;

  @Transactional
  public OrderResult createOrder(Long customerId,
      List<OrderItemCommand> orderItems,
      PaymentMethod paymentMethod) {
    Order order = Order.createOrder(customerId);

    for (OrderItemCommand orderItem : orderItems) {
      ProductResult product = productService.findProduct(orderItem.productId());
      order.addOrderItem(orderItem.productId(), orderItem.quantity(), product.salesPrice());
    }

    order.initPayment(paymentMethod);

    return saveOrder(order);
  }

  @Transactional
  public OrderResult completePayment(Long orderId, boolean success) {
    Order order = findOrderById(orderId);
    if (success) {
      order.completePayment();
      decreaseStock(order);
    } else {
      order.failPayment();
    }
    return saveOrder(order);
  }

  @Transactional
  public OrderResult completeOrder(Long orderId) {
    Order order = findOrderById(orderId);
    order.complete();

    return saveOrder(order);
  }

  @Transactional
  public OrderResult cancelOrder(Long orderId) {
    Order order = findOrderById(orderId);
    order.cancel();

    increaseStock(order);

    return saveOrder(order);
  }

  private void increaseStock(Order order) {
    for (OrderItem orderItem : order.getOrderItems()) {
      productService.increaseStock(orderItem.getProductId(), orderItem.getQuantity());
    }
  }


  private Order findOrderById(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
  }

  private void decreaseStock(Order order) {
    for (OrderItem orderItem : order.getOrderItems()) {
      productService.decreaseStock(orderItem.getProductId(), orderItem.getQuantity());
    }
  }

  private OrderResult saveOrder(Order order) {
    return OrderResult.from(orderRepository.save(order));
  }
}
