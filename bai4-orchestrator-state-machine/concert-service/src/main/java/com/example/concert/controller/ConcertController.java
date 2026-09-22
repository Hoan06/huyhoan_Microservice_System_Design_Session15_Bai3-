package com.example.concert.controller;
import com.example.concert.service.SeatReservationService; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/reservations") public class ConcertController { private final SeatReservationService service; public ConcertController(SeatReservationService service){this.service=service;} @PostMapping public boolean reserve(@RequestBody ReservationRequest request){return service.reserve(request.concertCode(),request.ticketQuantity());} public record ReservationRequest(String concertCode,int ticketQuantity){} }
