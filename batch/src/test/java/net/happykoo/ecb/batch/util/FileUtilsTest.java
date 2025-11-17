package net.happykoo.ecb.batch.util;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Slf4j
class FileUtilsTest {

  @Value("classpath:/data/products_for_upload.csv")
  private Resource csvResource;

  @Test
  @DisplayName("splitCsv 테스트")
  void splitCsvTest() throws IOException {
    List<File> files = FileUtils.splitCsv(csvResource.getFile(), 2);
    assertThat(files).hasSize(2);
    files.stream()
        .map(File::getName)
        .forEach(log::info);
  }
}
