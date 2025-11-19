package net.happykoo.ecb.api.domain.order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.happykoo.ecb.api.domain.payment.Payment;
import net.happykoo.ecb.api.domain.payment.PaymentMethod;
import net.happykoo.ecb.api.domain.payment.PaymentStatus;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long orderId;

  private LocalDateTime orderDate;

  private Long customerId;

  @Enumerated(value = EnumType.STRING)
  private OrderStatus orderStatus;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @Setter(AccessLevel.NONE)
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
  @Setter(AccessLevel.NONE)
  private Payment payment;

  public static Order createOrder(Long customerId) {
    return new Order(null, LocalDateTime.now(), customerId, OrderStatus.PENDING_PAYMENT,
        new ArrayList<>(), null);
  }

  public OrderItem addOrderItem(String productId, Integer quantity, Integer unitPrice) {
    OrderItem orderItem = OrderItem.createOrderItem(productId, quantity, unitPrice, this);
    orderItems.add(orderItem);
    return orderItem;
  }

  public void initPayment(PaymentMethod paymentMethod) {
    payment = Payment.createPayment(paymentMethod, calculateTotalAmount(), this);
  }

  public void completePayment() {
    if (orderStatus != OrderStatus.PENDING_PAYMENT) {
      throw new IllegalOrderStateException("결제 처리를 처리할 수 없는 상태입니다.");
    }
    payment.complete();
    orderStatus = OrderStatus.PROCESSING;
  }

  public void failPayment() {
    if (orderStatus != OrderStatus.PENDING_PAYMENT) {
      throw new IllegalOrderStateException("결제 처리를 처리할 수 없는 상태입니다.");
    }
    payment.fail();
    orderStatus = OrderStatus.PROCESSING;
  }

  public Long countProducts() {
    return (long) orderItems.size();
  }

  public Long calculateTotalItemQuantity() {
    return orderItems.stream()
        .mapToLong(OrderItem::getQuantity)
        .sum();
  }

  public Integer calculateTotalAmount() {
    return orderItems.stream()
        .mapToInt(item -> item.getUnitPrice() * item.getQuantity())
        .sum();
  }

  public PaymentStatus getPaymentStatus() {
    return payment.getPaymentStatus();
  }

  public Long getPaymentId() {
    return payment.getPaymentId();
  }

  public PaymentMethod getPaymentMethod() {
    return payment.getPaymentMethod();
  }

  public LocalDateTime getPaymentDate() {
    return payment.getPaymentDate();
  }

  public boolean isPaymentSuccess() {
    return payment.isSuccess();
  }

  public void complete() {
    if (orderStatus != OrderStatus.PROCESSING) {
      throw new IllegalOrderStateException("처리 중인 주문만 완료 처리할 수 있습니다.");
    }
    if (!isPaymentSuccess()) {
      throw new IllegalOrderStateException("결제 완료가 되지 않은 주문은 완료 처리할 수 없습니다.");
    }
    orderStatus = OrderStatus.COMPLETED;
  }

  public void cancel() {
    if (orderStatus == OrderStatus.COMPLETED) {
      throw new IllegalOrderStateException("이미 완료된 주문은 취소할 수 없습니다.");
    }
    payment.cancel();
    orderStatus = OrderStatus.CANCELED;
  }
}
