package net.happykoo.ecb.batch.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class ReflectionUtilsTest {

  private static class TestClass {

    private String field1;
    private int field2;
    public static final String field3 = "";
  }

  @Test
  void testGetFieldNames() {
    List<String> fieldNames = ReflectionUtils.getFieldName(TestClass.class);

    assertThat(fieldNames).hasSize(2)
        .containsExactly("field1", "field2")
        .doesNotContain("field3");
  }
}