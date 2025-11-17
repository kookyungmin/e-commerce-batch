package net.happykoo.ecb.batch.service.file;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import net.happykoo.ecb.batch.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;

class SplitFilePartitionerTest {

  @Test
  void testMultipleFiles() {
    List<File> files = createTempFile(3);
    SplitFilePartitioner partitioner = new SplitFilePartitioner(files);

    Map<String, ExecutionContext> result = partitioner.partition(3);
    assertThat(result).hasSize(3)
        .isEqualTo(Map.of(
            "partition0", new ExecutionContext(Map.of("file", files.get(0))),
            "partition1", new ExecutionContext(Map.of("file", files.get(1))),
            "partition2", new ExecutionContext(Map.of("file", files.get(2)))
        ));
  }

  @Test
  void testEmpty() {
    SplitFilePartitioner partitioner = new SplitFilePartitioner(new ArrayList<>());

    Map<String, ExecutionContext> result = partitioner.partition(2);
    assertThat(result).isEmpty();
  }

  private List<File> createTempFile(int count) {
    return IntStream.range(0, count)
        .mapToObj(idx -> {
          try {
            return FileUtils.createTempFile("test", ".tmp");
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        })
        .toList();
  }
}