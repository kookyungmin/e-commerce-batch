package net.happykoo.ecb.api.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

  private Product product;

  @BeforeEach
  void setup() {
    product = Product.of("1",
        93L,
        "가구",
        "침대_8",
        LocalDate.now(),
        LocalDate.now(),
        ProductStatus.OUT_OF_STOCK,
        "시몬스",
        "시몬스코리아",
        877031, 100,
        LocalDateTime.now(),
        LocalDateTime.now());
  }

  @ValueSource(ints = {1, 50, Integer.MAX_VALUE - 100})
  @ParameterizedTest
  @DisplayName("정상적으로 증가한 경우")
  void increaseStockTest(int value) {
    int beforeStock = product.getStockQuantity();
    product.increaseStock(value);

    assertThat(product.getStockQuantity()).isEqualTo(beforeStock + value);
  }

  @ValueSource(ints = {0, -1, Integer.MAX_VALUE})
  @ParameterizedTest
  @DisplayName("값이 비정상적인 경우")
  void increaseStockNegativeTest(int value) {
    assertThatThrownBy(() -> product.increaseStock(value))
        .isInstanceOf(StockQuantityArithmeticException.class);
  }

  @ValueSource(ints = {1, 50, 100})
  @ParameterizedTest
  @DisplayName("정상적으로 감소한 경우")
  void decreaseStockTest(int value) {
    int beforeStock = product.getStockQuantity();
    product.decreaseStock(value);

    assertThat(product.getStockQuantity()).isEqualTo(beforeStock - value);
  }

  @ValueSource(ints = {0, -1, 101})
  @ParameterizedTest
  @DisplayName("값이 비정상적인 경우")
  void decreaseStockNegativeTest(int value) {
    assertThatThrownBy(() -> product.decreaseStock(value))
        .isInstanceOf(StockQuantityArithmeticException.class);
  }
}