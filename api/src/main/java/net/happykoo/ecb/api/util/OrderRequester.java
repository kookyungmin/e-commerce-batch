package net.happykoo.ecb.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.happykoo.ecb.api.controller.dto.OrderItemRequest;
import net.happykoo.ecb.api.controller.dto.OrderRequest;
import net.happykoo.ecb.api.controller.dto.OrderResponse;
import net.happykoo.ecb.api.controller.dto.PaymentRequest;
import net.happykoo.ecb.api.domain.payment.PaymentMethod;

public class OrderRequester {

  private static final String BASE_URL = "http://localhost:8080/api/v1";
  private static final String PRODUCTS_URL = BASE_URL + "/products";
  private static final String ORDERS_URL = BASE_URL + "/orders";

  private static final PaymentMethod[] PAYMENT_METHODS = PaymentMethod.values();

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final Random random = new Random();
  private static final ExecutorService executor = Executors.newFixedThreadPool(300);

  private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
      .executor(executor)
      .build();

  public static void main(String[] args) {
    List<CompletableFuture<Void>> futures = new ArrayList<>();

    OBJECT_MAPPER.registerModule(new JavaTimeModule());
    OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    try {
      int page = 0;
      int size = 2000;
      boolean hasNextPage = true;

      while (hasNextPage && page < 5000) {
        String productJson = fetchProduct(page, size);
        JsonNode productsNode = OBJECT_MAPPER.readTree(productJson);
        JsonNode contentNode = productsNode.get("content");

        for (JsonNode productNode : contentNode) {
          String productId = productNode.get("productId").asText();
          int stockQuantity = productNode.get("stockQuantity").asInt();
          CompletableFuture<Void> future = processProduct(productId, stockQuantity);
          futures.add(future);
        }

        hasNextPage = !productsNode.get("last").asBoolean();
        page++;
      }
    } catch (Exception e) {
      System.out.println("ERROR ! " + e.getMessage());
    }

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    executor.shutdown();
  }

  private static CompletableFuture<Void> processProduct(String productId, int stockQuantity) {
    int quantity = Math.max((int) Math.floor(stockQuantity / 10.0), 1);
    int randomNum = random.nextInt(16); //0 ~ 15
    return createOrder(productId, quantity)
        .thenComposeAsync(orderResponse -> {
          if (orderResponse == null) {
            return CompletableFuture.completedFuture(null);
          }
          List<CompletableFuture<Void>> actions = new ArrayList<>();
          if (randomNum % 4 < 2) {
            //결제 완료 처리
            actions.add(completePayment(orderResponse.orderId(), randomNum % 2 == 0));
          }

          if (randomNum % 8 < 4) {
            //주문 완료 처리
            actions.add(completeOrder(orderResponse.orderId()));
          }

          if (randomNum % 16 < 8) {
            //주문 취소 처리
            actions.add(cancelOrder(orderResponse.orderId()));
          }
          return CompletableFuture.allOf(actions.toArray(new CompletableFuture[0]));
        }, executor);
  }

  private static CompletableFuture<OrderResponse> createOrder(String productId, int quantity) {
    OrderRequest orderRequest = OrderRequest.of(randomCustomerId(),
        List.of(OrderItemRequest.of(productId, quantity)),
        PAYMENT_METHODS[random.nextInt(PAYMENT_METHODS.length)]);
    try {
      String requestBody = OBJECT_MAPPER.writeValueAsString(orderRequest);
      return sendPostRequest(ORDERS_URL, requestBody)
          .thenApplyAsync(res -> {
            if (res.statusCode() == 200) {
              try {
                return OBJECT_MAPPER.readValue(res.body(), OrderResponse.class);
              } catch (JsonProcessingException e) {
                return null;
              }
            } else {
              return null;
            }
          }, executor);
    } catch (Exception e) {
      return CompletableFuture.completedFuture(null);
    }
  }


  private static CompletableFuture<HttpResponse<String>> sendPostRequest(String url,
      String requestBody) {
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();
    return HTTP_CLIENT.sendAsync(httpRequest,
        HttpResponse.BodyHandlers.ofString());
  }

  private static CompletableFuture<Void> completePayment(Long orderId, boolean success) {
    PaymentRequest paymentRequest = PaymentRequest.of(success);
    try {
      String requestBody = OBJECT_MAPPER.writeValueAsString(paymentRequest);
      return sendPostRequest(ORDERS_URL + "/" + orderId + "/payment", requestBody)
          .thenApply(res -> null);
    } catch (Exception e) {
      return CompletableFuture.completedFuture(null);
    }
  }

  private static CompletableFuture<Void> completeOrder(Long orderId) {
    return sendPostRequest(ORDERS_URL + "/" + orderId + "/complete", "")
        .thenApply(res -> null);
  }

  private static CompletableFuture<Void> cancelOrder(Long orderId) {
    return sendPostRequest(ORDERS_URL + "/" + orderId + "/cancel", "")
        .thenApply(res -> null);
  }

  private static String fetchProduct(int page, int size) throws IOException, InterruptedException {
    String url = String.format("%s?page=%d&size=%d&sort=productId,asc", PRODUCTS_URL, page, size);
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .GET()
        .build();
    HttpResponse<String> response = HTTP_CLIENT.send(httpRequest,
        HttpResponse.BodyHandlers.ofString());
    return response.body();
  }

  private static Long randomCustomerId() {
    return (long) (random.nextInt(1000) + 1);
  }
}