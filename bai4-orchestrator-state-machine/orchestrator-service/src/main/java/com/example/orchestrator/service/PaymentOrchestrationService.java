package com.example.orchestrator.service;

import com.example.orchestrator.model.BookingTransaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PaymentOrchestrationService {
  private final RestClient client;
  public PaymentOrchestrationService(RestClient.Builder builder, @Value("${payment-service.url}") String url) { client=builder.baseUrl(url).build(); }
  public boolean processPayment(BookingTransaction tx) { return Boolean.TRUE.equals(client.post().uri("/payments").contentType(MediaType.APPLICATION_JSON).body(tx).retrieve().body(Boolean.class)); }
  public void refund(BookingTransaction tx) { client.post().uri("/payments/{id}/refund",tx.getBookingId()).retrieve().toBodilessEntity(); }
}
