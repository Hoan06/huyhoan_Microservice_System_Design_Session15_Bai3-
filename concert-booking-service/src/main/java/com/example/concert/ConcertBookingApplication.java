package com.example.concert;

import com.example.concert.model.ConcertBookingEvent;
import com.example.concert.service.BookingPublisherService;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class ConcertBookingApplication {
  public static void main(String[] args) { SpringApplication.run(ConcertBookingApplication.class, args); }
  @Bean NewTopic concertEventsTopic() { return TopicBuilder.name("concert-events").partitions(1).replicas(1).build(); }
  @Bean CommandLineRunner publishDemo(BookingPublisherService publisher, @Value("${app.demo.publish-on-startup:true}") boolean enabled) {
    return args -> { if (enabled) publisher.publish(new ConcertBookingEvent("CONCERT-2024-999", "LIVE-HCM-2024", "nguyenvanA@email.com", 3)); };
  }
}
