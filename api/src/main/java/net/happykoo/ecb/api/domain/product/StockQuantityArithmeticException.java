package net.happykoo.ecb.api.domain.product;

public class StockQuantityArithmeticException extends ArithmeticException {

  public StockQuantityArithmeticException() {
    super("재고 수정을 위한 값이 비정상적입니다.");
  }
}
