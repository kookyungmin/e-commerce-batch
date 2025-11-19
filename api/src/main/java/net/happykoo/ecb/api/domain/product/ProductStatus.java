package net.happykoo.ecb.api.domain.product;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductStatus {
  AVAILABLE("판매 중"),
  OUT_OF_STOCK("품절"),
  DISCONTINUED("판매 종료");
  final String desc;
}
