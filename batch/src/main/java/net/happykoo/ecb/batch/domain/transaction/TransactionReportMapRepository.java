package net.happykoo.ecb.batch.domain.transaction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionReportMapRepository {

  private final Map<String, TransactionReport> reportMap = new ConcurrentHashMap<>();

  public void put(TransactionReport report) {
    String key = report.getTransactionDate() + "|" + report.getTransactionType();
    reportMap.compute(key, (k, r) -> {
      if (r == null) {
        return report;
      }
      r.add(report);
      return r;
    });
  }

  public List<TransactionReport> getTransactionReports() {
    return reportMap.values()
        .stream()
        .toList();
  }
}
