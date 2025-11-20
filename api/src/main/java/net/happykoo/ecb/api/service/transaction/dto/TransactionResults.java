package net.happykoo.ecb.api.service.transaction.dto;

import java.util.List;
import net.happykoo.ecb.api.domain.transaction.report.TransactionReport;

public record TransactionResults(List<TransactionResult> results) {

  public static TransactionResults from(List<TransactionReport> reports) {
    return new TransactionResults(reports.stream()
        .map(TransactionResult::from)
        .toList());
  }
}
