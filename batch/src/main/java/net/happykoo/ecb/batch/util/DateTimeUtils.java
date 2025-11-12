package net.happykoo.ecb.batch.util;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

import java.time.LocalDate;

public class DateTimeUtils {

  public static LocalDate toLocalDate(String date) {
    return LocalDate.parse(date, ISO_LOCAL_DATE);
  }

}
