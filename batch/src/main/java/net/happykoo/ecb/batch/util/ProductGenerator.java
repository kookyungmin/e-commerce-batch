package net.happykoo.ecb.batch.util;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import net.happykoo.ecb.batch.domain.product.ProductStatus;
import net.happykoo.ecb.batch.dto.ProductUploadCsvRow;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

@Slf4j
public class ProductGenerator {

  private static final Random RANDOM = new Random();

  public static void main(String[] args) {
    String csvFilePath = "batch/data/random_product.csv";
    int recordCount = 10_000_000;
//    int recordCount = 10;

    try (FileWriter fileWriter = new FileWriter(csvFilePath);
        CSVPrinter printer = new CSVPrinter(fileWriter, CSVFormat.DEFAULT.builder()
            .setHeader(
                ReflectionUtils.getFieldNames(ProductUploadCsvRow.class).toArray(String[]::new))
            .build())) {
      for (int i = 0; i < recordCount; i++) {
        printer.printRecord(generateProductCsvRowRecord());
        if (i % 100_000 == 0) {
          log.info("Generated {} records", i);
        }
      }

    } catch (IOException e) {
      log.error("IO Exception! {}", e.getMessage());
    }
  }

  private static Object[] generateProductCsvRowRecord() {
    ProductUploadCsvRow row = randomProductCsvRow();
    return new Object[]{
        row.getSellerId(),
        row.getCategory(),
        row.getProductName(),
        row.getSalesStartDate(),
        row.getSalesEndDate(),
        row.getProductStatus(),
        row.getBrand(),
        row.getManufacturer(),
        row.getSalesPrice(),
        row.getStockQuantity()
    };
  }

  private static ProductUploadCsvRow randomProductCsvRow() {
    final String[] CATEGORIES = {"가전", "가구", "전자기기", "식품", "의류", "스포츠", "화장품", "자동차", "완구"};
    final String[] PRODUCT_NAMES = {"TV", "침대", "노트북", "닭가슴살", "코트", "축구공", "스킨", "K5", "색연필"};
    final String[] BRAND = {"삼성", "시몬스", "Apple", "득근파티", "시스템옴므", "나이키", "한국콜마", "기아", "손오공"};
    final String[] MANUFACTURERS = {"삼성전자", "시몬스코리아", "애플코리아", "득근파티공장", "옴므코리아", "나이키코리아", "콜마공장",
        "기아공장", "손오공공장"};
    final String[] STATUS = Arrays.stream(ProductStatus.values())
        .map(Enum::name)
        .toArray(String[]::new);

    int randomIndex = RANDOM.nextInt(CATEGORIES.length);

    return ProductUploadCsvRow.of(
        randomSellerId(),
        CATEGORIES[randomIndex],
        PRODUCT_NAMES[randomIndex] + "_" + RANDOM.nextInt(1, 10),
        randomDate(2020, 2023),
        randomDate(2024, 2026),
        STATUS[RANDOM.nextInt(STATUS.length)],
        BRAND[randomIndex],
        MANUFACTURERS[randomIndex],
        randomSalesPrice(),
        randomStockQuantity()
    );
  }

  private static int randomStockQuantity() {
    return RANDOM.nextInt(1, 10_001);
  }

  private static int randomSalesPrice() {
    return RANDOM.nextInt(10_000, 1_000_001);
  }

  private static String randomDate(int startYear, int endYear) {
    int year = RANDOM.nextInt(startYear, endYear + 1);
    int month = RANDOM.nextInt(1, 13);
    int day = RANDOM.nextInt(1, 29);

    return LocalDate.of(year, month, day).toString();
  }

  private static Long randomSellerId() {
    return RANDOM.nextLong(1, 101);
  }
}
