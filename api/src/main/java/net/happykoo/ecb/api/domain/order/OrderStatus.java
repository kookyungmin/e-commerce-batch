package net.happykoo.ecb.api.domain.order;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
  PENDING_PAYMENT("결제 대기 중"),
  PROCESSING("처리 중"),
  COMPLETED("주문 완료"),
  CANCELED("주문 취소");

  private final String desc;
}
