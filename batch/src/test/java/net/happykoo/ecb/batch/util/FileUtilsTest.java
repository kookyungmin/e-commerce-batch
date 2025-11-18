package net.happykoo.ecb.batch.util;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Slf4j
class FileUtilsTest {

  @Value("classpath:/data/products_for_upload.csv")
  private Resource csvResource;

  @TempDir
  private Path tempDir;

  @Test
  @DisplayName("splitCsv 테스트")
  void splitCsvTest() throws IOException {
    List<File> files = FileUtils.splitCsv(csvResource.getFile(), 2);
    assertThat(files).hasSize(2);
    files.stream()
        .map(File::getName)
        .forEach(log::info);
  }

  @Test
  @DisplayName("mergeFile 테스트")
  void mergeFileTest() throws IOException {
    List<File> files = List.of(
        createTempFile("test1", "happykoo1\n"),
        createTempFile("test2", "happykoo2\n"),
        createTempFile("test3", "happykoo3\n")
    );
    File outputFile = new File(tempDir.toFile(), "output.txt");
    String header = "content";

    FileUtils.mergeFile(header, files, outputFile);

    assertThat(Files.readAllLines(outputFile.toPath()))
        .containsExactly("content", "happykoo1", "happykoo2", "happykoo3");
  }

  private File createTempFile(String fileName, String content) throws IOException {
    File file = new File(tempDir.toFile(), fileName);
    Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
    return file;
  }
}
