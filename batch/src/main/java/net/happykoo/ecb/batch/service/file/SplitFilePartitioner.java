package net.happykoo.ecb.batch.service.file;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

@RequiredArgsConstructor
public class SplitFilePartitioner implements Partitioner {

  private final List<File> splitFiles;


  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    Map<String, ExecutionContext> partitionMap = new HashMap<>();
    for (int i = 0; i < splitFiles.size(); i++) {
      ExecutionContext executionContext = new ExecutionContext();
      executionContext.put("file", splitFiles.get(i).getAbsolutePath());
      partitionMap.put("partition" + i, executionContext);
    }
    return partitionMap;
  }
}
