package net.happykoo.ecb.api.util;

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

  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

  public static void main(String[] args) {
    int maxWorker = 20;
    ExecutorService executor = Executors.newFixedThreadPool(maxWorker);
    List<CompletableFuture<Void>> futures = new ArrayList<>();

    OBJECT_MAPPER.registerModule(new JavaTimeModule());
    OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    try {
      int page = 0;
      int size = 1000;
      boolean hasNextPage = true;

      while (hasNextPage && page < 10000) {
        String productJson = fetchProduct(page, size);
        JsonNode productsNode = OBJECT_MAPPER.readTree(productJson);
        JsonNode contentNode = productsNode.get("content");

        for (JsonNode productNode : contentNode) {
          String productId = productNode.get("productId").asText();
          int stockQuantity = productNode.get("stockQuantity").asInt();
          CompletableFuture<Void> future = CompletableFuture.runAsync(
              () -> processProduct(productId, stockQuantity), executor);
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

  private static void processProduct(String productId, int stockQuantity) {
    int quantity = Math.max((int) Math.floor(stockQuantity / 10.0), 1);
    int randomNum = random.nextInt(16); //0 ~ 15

    OrderResponse orderResponse = createOrder(productId, quantity);
    if (orderResponse != null) {
      if (randomNum % 4 < 2) {
        //결제 완료 처리
        completePayment(orderResponse.orderId(), randomNum % 2 == 0);
      }

      if (randomNum % 8 < 4) {
        //주문 완료 처리
        completeOrder(orderResponse.orderId());
      }

      if (randomNum % 16 < 8) {
        //주문 취소 처리
        cancelOrder(orderResponse.orderId());
      }
    }
  }

  private static OrderResponse createOrder(String productId, int quantity) {
    OrderRequest orderRequest = OrderRequest.of(randomCustomerId(),
        List.of(OrderItemRequest.of(productId, quantity)),
        PAYMENT_METHODS[random.nextInt(PAYMENT_METHODS.length)]);
    try {
      String requestBody = OBJECT_MAPPER.writeValueAsString(orderRequest);
      HttpResponse<String> response = sendPostRequest(ORDERS_URL, requestBody);
      if (response.statusCode() == 200) {
        OrderResponse orderResponse = OBJECT_MAPPER.readValue(response.body(), OrderResponse.class);
        return orderResponse;
      } else {
//        System.out.println("주문 생성 중 오류 발생");
        return null;
      }
    } catch (Exception e) {
//      System.out.println("ERROR ! " + e.getMessage());
      return null;
    }
  }

  private static HttpResponse<String> sendPostRequest(String url, String requestBody)
      throws IOException, InterruptedException {
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
        .build();
    HttpResponse<String> response = HTTP_CLIENT.send(httpRequest,
        HttpResponse.BodyHandlers.ofString());
    return response;
  }

  private static void completePayment(Long orderId, boolean success) {
    PaymentRequest paymentRequest = PaymentRequest.of(success);
    try {
      String requestBody = OBJECT_MAPPER.writeValueAsString(paymentRequest);
      HttpResponse<String> response = sendPostRequest(ORDERS_URL + "/" + orderId + "/payment",
          requestBody);
      if (response.statusCode() != 200) {
//        System.out.println("결제처리 중 오류 발생");
      }
    } catch (Exception e) {
//      System.out.println("ERROR ! " + e.getMessage());
    }
  }

  private static void completeOrder(Long orderId) {
    try {
      HttpResponse<String> response = sendPostRequest(ORDERS_URL + "/" + orderId + "/complete",
          "");
      if (response.statusCode() != 200) {
//        System.out.println("주문 완료 중 오류 발생");
      }
    } catch (Exception e) {
//      System.out.println("ERROR ! " + e.getMessage());
    }
  }

  private static void cancelOrder(Long orderId) {
    try {
      HttpResponse<String> response = sendPostRequest(ORDERS_URL + "/" + orderId + "/cancel",
          "");
      if (response.statusCode() != 200) {
//        System.out.println("주문 취소 중 오류 발생");
      }
    } catch (Exception e) {
//      System.out.println("ERROR ! " + e.getMessage());
    }
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
