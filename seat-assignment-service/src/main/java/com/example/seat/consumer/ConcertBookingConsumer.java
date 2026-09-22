package com.example.seat.consumer;

import com.example.seat.model.ConcertBookingEvent;
import com.example.seat.model.SeatReservedEvent;
import com.example.seat.producer.SeatEventProducer;
import com.example.seat.service.SeatAssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ConcertBookingConsumer {
  private static final Logger log = LoggerFactory.getLogger(ConcertBookingConsumer.class);
  private final SeatAssignmentService seatService; private final SeatEventProducer producer; private final ObjectMapper mapper;
  public ConcertBookingConsumer(SeatAssignmentService seatService, SeatEventProducer producer, ObjectMapper mapper) { this.seatService = seatService; this.producer = producer; this.mapper = mapper; }
  @KafkaListener(topics = "concert-events", groupId = "seat-assignment-group")
  public void handleConcertBooking(ConsumerRecord<String, String> record) {
    try {
      ConcertBookingEvent event = mapper.readValue(record.value(), ConcertBookingEvent.class);
      String correlationId = event.correlationId();
      log.info("[SeatService] Received event with correlationId: {}", correlationId);
      seatService.reserveSeat(event);
      log.info("[SeatService] Seat reserved successfully for correlationId: {}", correlationId);
      producer.publishSeatReserved(new SeatReservedEvent(correlationId, event.customerEmail(), event.ticketQuantity()));
      log.info("[SeatService] Publishing SeatReserved event with correlationId: {} to topic: seat-events", correlationId);
    } catch (Exception e) { log.error("Error processing concert booking", e); }
  }
}
