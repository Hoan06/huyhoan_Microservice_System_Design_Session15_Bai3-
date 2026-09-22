package com.example.seat.model;

public record ConcertBookingEvent(String correlationId, String concertCode, String customerEmail, int ticketQuantity) { }
