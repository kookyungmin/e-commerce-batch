package net.happykoo.ecb.batch.service.product;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.batch.domain.file.PartitionedFileRepository;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDownloadPartitioner implements Partitioner {

  private final ProductService productService;
  private final PartitionedFileRepository partitionedFileRepository;

  @Override
  public Map<String, ExecutionContext> partition(int gridSize) {
    //전체 sorting 하여 ID 가져옴 예) [1, 2, 3, 4, 5]
    List<String> productIds = productService.getProductIds()
        .stream()
        .sorted()
        .toList();

    Map<String, ExecutionContext> result = new HashMap<>();

    int minIdx = 0; //ex) 0
    int maxIdx = productIds.size() - 1; //ex) 4

    //gridSize가 2라면, targetSize = (4 - 0) / 2 + 1 = 3
    int targetSize = (maxIdx - minIdx) / gridSize + 1;
    int partitionNumber = 0;
    int start = minIdx;
    int end = start + targetSize - 1;

    //[1, 2, 3], [4, 5] 로 partition 분할 + partition이 사용할 file도 생성
    while (start <= maxIdx) {
      ExecutionContext context = new ExecutionContext();
      String partitionKey = "partition" + partitionNumber;
      result.put(partitionKey, context);

      if (end > maxIdx) {
        end = maxIdx;
      }

      context.putString("minId", productIds.get(start));
      context.putString("maxId", productIds.get(end));

      try {
        File partitionedFile = partitionedFileRepository.createTempFile(partitionKey,
            partitionKey + "_", ".csv");
        context.put("file", partitionedFile);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

      start += targetSize;
      end += targetSize;
      partitionNumber++;
    }

    return result;
  }
}
