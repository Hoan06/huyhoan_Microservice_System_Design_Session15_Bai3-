package com.example.orchestrator.service;

import com.example.orchestrator.model.BookingTransaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ReservationOrchestrationService {
  private final RestClient client;
  public ReservationOrchestrationService(RestClient.Builder builder, @Value("${concert-service.url}") String url) { client=builder.baseUrl(url).build(); }
  public boolean reserveSeats(BookingTransaction tx) { return Boolean.TRUE.equals(client.post().uri("/reservations").contentType(MediaType.APPLICATION_JSON).body(tx).retrieve().body(Boolean.class)); }
}
