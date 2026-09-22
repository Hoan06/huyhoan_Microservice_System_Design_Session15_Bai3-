package com.example.concert.model;

public record ConcertBookingEvent(String correlationId, String concertCode, String customerEmail, int ticketQuantity) { }
