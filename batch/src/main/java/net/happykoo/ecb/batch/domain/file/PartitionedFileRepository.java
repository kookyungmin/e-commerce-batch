package net.happykoo.ecb.batch.domain.file;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.happykoo.ecb.batch.util.FileUtils;
import org.springframework.stereotype.Repository;

@Repository
public class PartitionedFileRepository {

  private final Map<String, File> fileMap = new ConcurrentHashMap<>();

  public File createTempFile(String partitionKey, String prefix, String suffix) throws IOException {
    File file = FileUtils.createTempFile(prefix, suffix);

    fileMap.put(partitionKey, file);

    return file;
  }

  public List<File> getFiles() {
    return fileMap.values()
        .stream()
        .sorted(Comparator.comparing(File::getName))
        .toList();
  }

}
