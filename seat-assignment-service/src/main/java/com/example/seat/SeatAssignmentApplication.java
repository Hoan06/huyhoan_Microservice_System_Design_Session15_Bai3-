package com.example.seat;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class SeatAssignmentApplication {
  public static void main(String[] args) { SpringApplication.run(SeatAssignmentApplication.class, args); }
  @Bean NewTopic seatEventsTopic() { return TopicBuilder.name("seat-events").partitions(1).replicas(1).build(); }
}
