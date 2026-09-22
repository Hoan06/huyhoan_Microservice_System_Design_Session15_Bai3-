package com.example.concert.service;

import com.example.concert.model.ConcertBookingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingPublisherService {
  private final KafkaTemplate<String, String> kafkaTemplate; private final ObjectMapper objectMapper;
  public BookingPublisherService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) { this.kafkaTemplate = kafkaTemplate; this.objectMapper = objectMapper; }
  public void publish(ConcertBookingEvent event) throws JsonProcessingException { kafkaTemplate.send("concert-events", event.correlationId(), objectMapper.writeValueAsString(event)); }
}
