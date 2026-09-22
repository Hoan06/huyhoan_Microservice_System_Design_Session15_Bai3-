package com.example.notification.model;

public record SeatReservedEvent(String correlationId, String customerEmail, int ticketQuantity) { }
