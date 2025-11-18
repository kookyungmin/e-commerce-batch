package net.happykoo.ecb.batch.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DateTimeUtilsTest {

  @Test
  void testToLocalDate() {
    // given
    String dateString = "2022-03-24";

    // when
    LocalDate result = DateTimeUtils.toLocalDate(dateString);

    // then
    assertThat(result)
        .isEqualTo(java.time.LocalDate.of(2022, 3, 24));
  }

  @Test
  void testToLocalDateTime() {
    // given
    String dateTimeString = "2025-11-18 14:53:00.404";

    // when
    LocalDateTime result = DateTimeUtils.toLocalDateTime(dateTimeString);

    // then
    assertThat(result)
        .isEqualTo(java.time.LocalDateTime.of(2025, 11, 18, 14, 53, 0, 404_000_000));
  }

  @Test
  void testToString() {
    // given
    LocalDate date = LocalDate.of(2022, 3, 24);
    LocalDateTime dateTime = LocalDateTime.of(2025, 11, 18, 14, 53, 0, 404_000_000);

    // when
    String result = DateTimeUtils.toString(date);
    String resultTime = DateTimeUtils.toString(dateTime);

    // then
    assertThat(result)
        .isEqualTo("2022-03-24");
    assertThat(resultTime)
        .isEqualTo("2025-11-18 14:53:00.404");
  }
}