package com.example.seat.service;

import com.example.seat.model.ConcertBookingEvent;
import org.springframework.stereotype.Service;

@Service
public class SeatAssignmentService {
  /** Place to persist the reservation in a real database. */
  public void reserveSeat(ConcertBookingEvent event) {
    if (event.ticketQuantity() < 1) throw new IllegalArgumentException("ticketQuantity must be positive");
  }
}
