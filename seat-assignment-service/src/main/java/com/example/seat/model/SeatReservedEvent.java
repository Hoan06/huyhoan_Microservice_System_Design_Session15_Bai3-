package com.example.seat.model;

public record SeatReservedEvent(String correlationId, String customerEmail, int ticketQuantity) { }
