package net.happykoo.ecb.api.domain.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.happykoo.ecb.api.domain.order.Order;

@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long paymentId;

  @Enumerated(EnumType.STRING)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  private LocalDateTime paymentDate;

  private Integer amount;

  @OneToOne
  @JoinColumn(name = "order_id")
  @ToString.Exclude
  private Order order;

  public static Payment createPayment(PaymentMethod paymentMethod, Integer amount, Order order) {
    return new Payment(null,
        paymentMethod,
        PaymentStatus.PENDING,
        LocalDateTime.now(),
        amount,
        order);
  }

  public void complete() {
    if (paymentStatus != PaymentStatus.PENDING) {
      throw new IllegalPaymentStateException("결제 대기 상태에만 완료 처리가 가능합니다.");
    }
    this.paymentStatus = PaymentStatus.COMPLETED;
  }

  public void fail() {
    if (paymentStatus != PaymentStatus.PENDING) {
      throw new IllegalPaymentStateException("결제 대기 상태에만 실패 처리가 가능합니다.");
    }
    this.paymentStatus = PaymentStatus.FAILED;
  }

  public void cancel() {
    switch (paymentStatus) {
      case COMPLETED -> paymentStatus = PaymentStatus.REFUNDED;
      case PENDING, FAILED -> paymentStatus = PaymentStatus.CANCELED;
      case CANCELED -> throw new IllegalPaymentStateException("이미 취소가 완료되었습니다.");
      case REFUNDED -> throw new IllegalPaymentStateException("이미 환불이 완료되었습니다.");
    }
  }

  public boolean isSuccess() {
    return paymentStatus == PaymentStatus.COMPLETED;
  }
}
