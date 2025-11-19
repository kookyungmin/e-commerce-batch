package net.happykoo.ecb.api.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.api.domain.order.Order;
import net.happykoo.ecb.api.domain.order.OrderItem;
import net.happykoo.ecb.api.domain.order.OrderNotFoundException;
import net.happykoo.ecb.api.domain.payment.PaymentMethod;
import net.happykoo.ecb.api.repository.OrderRepository;
import net.happykoo.ecb.api.service.dto.OrderItemCommand;
import net.happykoo.ecb.api.service.dto.OrderResult;
import net.happykoo.ecb.api.service.dto.ProductResult;
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
    Order order = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);
    if (success) {
      order.completePayment();
      decreaseStock(order);
    } else {
      order.failPayment();
    }
    return saveOrder(order);
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
