package com.example.seat.producer;

import com.example.seat.model.SeatReservedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SeatEventProducer {
  private final KafkaTemplate<String, String> kafkaTemplate; private final ObjectMapper objectMapper;
  public SeatEventProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) { this.kafkaTemplate = kafkaTemplate; this.objectMapper = objectMapper; }
  public void publishSeatReserved(SeatReservedEvent event) throws JsonProcessingException { kafkaTemplate.send("seat-events", event.correlationId(), objectMapper.writeValueAsString(event)); }
}
