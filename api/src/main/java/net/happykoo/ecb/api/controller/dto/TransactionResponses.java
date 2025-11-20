package net.happykoo.ecb.api.controller.dto;

import java.util.List;
import net.happykoo.ecb.api.service.transaction.dto.TransactionResults;

public record TransactionResponses(List<TransactionResponse> reports) {

  public static TransactionResponses from(TransactionResults transactionResults) {
    return new TransactionResponses(transactionResults.results().stream()
        .map(TransactionResponse::from)
        .toList());
  }
}
