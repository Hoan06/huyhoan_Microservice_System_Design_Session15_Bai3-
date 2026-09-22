package com.example.payment;
import org.springframework.stereotype.Service;
@Service public class PaymentProcessingService { public boolean charge(long amount){ return amount>0; } public void refund(String bookingId) { } }
