package com.example.orchestrator;

import com.example.orchestrator.machine.ConcertBookingStateMachine;
import com.example.orchestrator.model.BookingTransaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OrchestratorApplication {
  public static void main(String[] args) { SpringApplication.run(OrchestratorApplication.class, args); }
  @Bean CommandLineRunner demo(ConcertBookingStateMachine machine) {
    return args -> machine.process(new BookingTransaction("CONCERT-2026-088", "LIVE-HCM-2026-ULTRA", "VIP-2024", "rika@email.com", 3, 5_500_000));
  }
}
