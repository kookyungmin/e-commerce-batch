package net.happykoo.ecb.api.controller.transaction;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import net.happykoo.ecb.api.controller.transaction.dto.TransactionResponses;
import net.happykoo.ecb.api.service.transaction.report.TransactionReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction/reports")
@RequiredArgsConstructor
public class TransactionReportController {

  private final TransactionReportService transactionReportService;

  @GetMapping
  public TransactionResponses getTransactionReports(
      @RequestParam("dt") @DateTimeFormat(iso = ISO.DATE)
      LocalDate date) {
    return TransactionResponses.from(transactionReportService.findByDate(date));
  }

}
