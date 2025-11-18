package net.happykoo.ecb.batch.job.product.download;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import net.happykoo.ecb.batch.domain.product.Product;
import net.happykoo.ecb.batch.domain.product.ProductStatus;
import net.happykoo.ecb.batch.jobconfig.BaseBatchIntegrationTest;
import net.happykoo.ecb.batch.service.product.ProductService;
import net.happykoo.ecb.batch.util.DateTimeUtils;
import net.happykoo.ecb.batch.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.batch.job.name=productDownloadJob"})
class ProductDownloadJobConfigurationTest extends BaseBatchIntegrationTest {

  @Autowired
  private ProductService productService;

  @Value("classpath:/data/products_downloaded_expected.csv")
  private Resource expectedResource;

  private File outputFile;

  @Test
  void testJob(@Autowired Job productDownloadJob) throws Exception {
    saveProduct();
    outputFile = FileUtils.createTempFile("product_downloaded", ".csv");
    JobParameters jobParameters = jobParameters();
    jobLauncherTestUtils.setJob(productDownloadJob);

    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    assertAll(() -> assertJobCompleted(jobExecution),
        () -> assertThat(Files.readString(Path.of(outputFile.getPath()))).isEqualTo(
            Files.readString(Path.of(expectedResource.getFile().getPath())))
    );
  }

  private void saveProduct() {
    productService.save(Product.of("1", 93L, "가구", "침대_8", DateTimeUtils.toLocalDate("2022-03-24"),
        DateTimeUtils.toLocalDate("2026-12-23"), ProductStatus.OUT_OF_STOCK, "시몬스", "시몬스코리아",
        877031, 466,
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404"),
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404")));
    productService.save(Product.of("2", 4L, "스포츠", "축구공_7", DateTimeUtils.toLocalDate("2021-09-11"),
        DateTimeUtils.toLocalDate("2025-03-09"), ProductStatus.OUT_OF_STOCK, "나이키", "나이키코리아", 74763,
        8945,
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404"),
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404")));
    productService.save(Product.of("3", 41L, "가전", "TV_8", DateTimeUtils.toLocalDate("2022-02-16"),
        DateTimeUtils.toLocalDate("2024-01-05"), ProductStatus.OUT_OF_STOCK, "삼성", "삼성전자", 352205,
        9216,
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404"),
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404")));
    productService.save(
        Product.of("4", 50L, "전자기기", "노트북_7", DateTimeUtils.toLocalDate("2023-01-04"),
            DateTimeUtils.toLocalDate("2024-03-23"), ProductStatus.OUT_OF_STOCK, "Apple", "애플코리아",
            832959, 7066,
            DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404"),
            DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404")));
    productService.save(Product.of("5", 10L, "가전", "TV_6", DateTimeUtils.toLocalDate("2020-11-25"),
        DateTimeUtils.toLocalDate("2026-07-25"), ProductStatus.DISCONTINUED, "삼성", "삼성전자", 350201,
        2947,
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404"),
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404")));
    productService.save(Product.of("6", 31L, "가전", "TV_6", DateTimeUtils.toLocalDate("2023-11-03"),
        DateTimeUtils.toLocalDate("2025-01-17"), ProductStatus.DISCONTINUED, "삼성", "삼성전자", 480075,
        1883,
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404"),
        DateTimeUtils.toLocalDateTime("2025-11-18 14:53:00.404")));
  }

  private JobParameters jobParameters() {
    return new JobParametersBuilder()
        .addJobParameter("outputFilePath",
            new JobParameter<>(outputFile.getPath(), String.class, false))
        .addJobParameter("gridSize",
            new JobParameter<>(6, Integer.class, false))
        .toJobParameters();
  }
}