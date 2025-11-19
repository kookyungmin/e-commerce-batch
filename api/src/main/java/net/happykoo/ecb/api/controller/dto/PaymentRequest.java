package net.happykoo.ecb.api.controller.dto;

public record PaymentRequest(boolean success) {
  public static PaymentRequest of(boolean success) {
    return new PaymentRequest(success);
  }
}
