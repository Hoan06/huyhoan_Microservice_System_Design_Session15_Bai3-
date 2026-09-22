package com.example.notification.consumer;

import com.example.notification.model.SeatReservedEvent;
import com.example.notification.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SeatReservedConsumer {
  private static final Logger log = LoggerFactory.getLogger(SeatReservedConsumer.class);
  private final ObjectMapper mapper; private final NotificationService notificationService;
  public SeatReservedConsumer(ObjectMapper mapper, NotificationService notificationService) { this.mapper = mapper; this.notificationService = notificationService; }
  @KafkaListener(topics = "seat-events", groupId = "notification-group")
  public void handleSeatReserved(ConsumerRecord<String, String> record) {
    try {
      SeatReservedEvent event = mapper.readValue(record.value(), SeatReservedEvent.class);
      notificationService.sendConfirmation(event);
      log.info("[NotifyService] Received confirmation for correlationId: {} - Sending email to {}", event.correlationId(), event.customerEmail());
    } catch (Exception e) { log.error("Error processing seat reserved event", e); }
  }
}
