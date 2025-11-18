package net.happykoo.ecb.batch.util;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd HH:mm:ss.SSS");

  public static LocalDate toLocalDate(String date) {
    return LocalDate.parse(date, ISO_LOCAL_DATE);
  }

  public static LocalDateTime toLocalDateTime(String dateTime) {
    return LocalDateTime.parse(dateTime, dateTimeFormatter);
  }

  public static String toString(LocalDate date) {
    return date.format(ISO_LOCAL_DATE);
  }

  public static String toString(LocalDateTime dateTime) {
    return dateTime.format(dateTimeFormatter);
  }
}
