package com.example.concert.service;
import org.springframework.stereotype.Service;
@Service public class SeatReservationService { public boolean reserve(String concertCode,int quantity){return concertCode!=null && !concertCode.isBlank() && quantity>0;} }
