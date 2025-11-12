package net.happykoo.ecb.batch.util;

import java.time.Instant;
import java.util.UUID;

public class RandomUtils {

  public static String getRandomId() {
    return Instant.now().toEpochMilli() + "_" + UUID.randomUUID();
  }
}
