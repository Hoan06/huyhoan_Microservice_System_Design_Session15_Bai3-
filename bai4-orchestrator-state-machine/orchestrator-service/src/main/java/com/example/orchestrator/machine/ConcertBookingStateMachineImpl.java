package com.example.orchestrator.machine;

import com.example.orchestrator.listener.StateChangeListener;
import com.example.orchestrator.model.*;
import com.example.orchestrator.service.PaymentOrchestrationService;
import com.example.orchestrator.service.ReservationOrchestrationService;
import org.slf4j.Logger; import org.slf4j.LoggerFactory; import org.springframework.stereotype.Service; import org.springframework.web.client.ResourceAccessException;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConcertBookingStateMachineImpl implements ConcertBookingStateMachine {
  private static final Logger log=LoggerFactory.getLogger(ConcertBookingStateMachineImpl.class);
  private final PaymentOrchestrationService payment; private final ReservationOrchestrationService reservation; private final StateChangeListener listener;
  private final ConcurrentHashMap<String, BookingState> states=new ConcurrentHashMap<>(); private final RetryPolicy retry=new RetryPolicy(3,2000);
  public ConcertBookingStateMachineImpl(PaymentOrchestrationService payment, ReservationOrchestrationService reservation, StateChangeListener listener) {this.payment=payment;this.reservation=reservation;this.listener=listener;}
  public void process(BookingTransaction tx) {
    states.putIfAbsent(tx.getBookingId(),tx.getCurrentState());
    switch(tx.getCurrentState()) {
      case INITIATED -> { transition(tx,BookingState.PAYMENT_PENDING,BookingEvent.PROCESS_PAYMENT); processPayment(tx); }
      case PAYMENT_COMPLETED -> { transition(tx,BookingState.SEAT_RESERVING,BookingEvent.RESERVE_SEATS); processReservation(tx); }
      case BOOKING_CONFIRMED -> log.info("[Orchestrator] Final State: BOOKING_CONFIRMED for booking {}",tx.getBookingId());
      case CANCELLED -> log.info("[Orchestrator] Transaction {} is CANCELLED",tx.getBookingId());
      default -> { }
    }
  }
  private void processPayment(BookingTransaction tx) {
    for(int attempt=1;attempt<=retry.maxAttempts();attempt++) try {
      log.info("[Orchestrator] RetryPolicy: Activity 'processPayment' - Attempt {}/{}",attempt,retry.maxAttempts());
      if(payment.processPayment(tx)) { tx.setPaymentCaptured(true); transition(tx,BookingState.PAYMENT_COMPLETED,BookingEvent.PAYMENT_SUCCESS); process(tx); return; }
      cancel(tx,BookingEvent.PAYMENT_FAILED); return;
    } catch(ResourceAccessException timeout) { if(attempt==retry.maxAttempts()) { cancel(tx,BookingEvent.PAYMENT_FAILED); return; } sleepBeforeRetry();
    } catch(Exception businessFailure) { cancel(tx,BookingEvent.PAYMENT_FAILED); return; }
  }
  private void processReservation(BookingTransaction tx) { try { if(reservation.reserveSeats(tx)) { transition(tx,BookingState.BOOKING_CONFIRMED,BookingEvent.RESERVATION_SUCCESS); process(tx); } else cancel(tx,BookingEvent.RESERVATION_FAILED); } catch(Exception e) { cancel(tx,BookingEvent.RESERVATION_FAILED); } }
  private void cancel(BookingTransaction tx,BookingEvent event) { transition(tx,BookingState.CANCELLED,event); if(tx.isPaymentCaptured()) { log.info("[Orchestrator] Compensation triggered for booking: {}",tx.getBookingId()); payment.refund(tx); } process(tx); }
  private void transition(BookingTransaction tx,BookingState newState,BookingEvent event) { BookingState old=states.put(tx.getBookingId(),newState); tx.setCurrentState(newState); listener.onStateChanged(tx.getBookingId(),old,newState,event); }
  private void sleepBeforeRetry() { try { Thread.sleep(retry.delayMs()); } catch(InterruptedException e) { Thread.currentThread().interrupt(); throw new IllegalStateException("Payment retry interrupted",e); } }
}
