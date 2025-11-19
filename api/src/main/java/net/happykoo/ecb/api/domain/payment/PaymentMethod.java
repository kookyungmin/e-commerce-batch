package net.happykoo.ecb.api.domain.payment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethod {
  CREDIT_CARD("신용카드"),
  DEBIT_CARD("직불카드"),
  PAYPAL("페이팔"),
  BANK_TRANSFER("계좌이체");
  private final String desc;
}
