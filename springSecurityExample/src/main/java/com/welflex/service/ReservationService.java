package com.welflex.service;

import java.util.List;

import com.welflex.model.Reservation;
import com.welflex.model.Ticket;

public interface ReservationService {

  Ticket bookFlight(Reservation reservation) throws NoSuchFlightException, NoSeatAvailableException;

  Ticket getReservation(Long reservationId) throws NoSuchReservationException;
  
  void cancelReservation(Long reservationId) throws NoSuchReservationException;

  List<Ticket> getReservations();
}
