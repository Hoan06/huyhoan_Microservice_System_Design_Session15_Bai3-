package com.example.orchestrator.model;

public class BookingTransaction {
  private final String bookingId, concertCode, customerId, customerEmail; private final int ticketQuantity; private final long amount;
  private BookingState currentState = BookingState.INITIATED; private boolean paymentCaptured;
  public BookingTransaction(String bookingId, String concertCode, String customerId, String customerEmail, int ticketQuantity, long amount) { this.bookingId=bookingId; this.concertCode=concertCode; this.customerId=customerId; this.customerEmail=customerEmail; this.ticketQuantity=ticketQuantity; this.amount=amount; }
  public String getBookingId(){return bookingId;} public String getConcertCode(){return concertCode;} public String getCustomerId(){return customerId;} public String getCustomerEmail(){return customerEmail;} public int getTicketQuantity(){return ticketQuantity;} public long getAmount(){return amount;}
  public BookingState getCurrentState(){return currentState;} public void setCurrentState(BookingState state){currentState=state;} public boolean isPaymentCaptured(){return paymentCaptured;} public void setPaymentCaptured(boolean captured){paymentCaptured=captured;}
}
