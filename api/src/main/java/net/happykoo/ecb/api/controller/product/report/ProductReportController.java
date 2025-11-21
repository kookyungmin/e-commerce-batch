package net.happykoo.ecb.api.controller.product.report;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.api.controller.product.report.dto.ProductReportResponse;
import net.happykoo.ecb.api.service.product.report.ProductReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/product/reports")
@RestController
@RequiredArgsConstructor
public class ProductReportController {

  private final ProductReportService productReportService;

  @GetMapping
  public ProductReportResponse getProductReport(
      @RequestParam("dt") @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
    return ProductReportResponse.of(productReportService.findReports(date));
  }
}
