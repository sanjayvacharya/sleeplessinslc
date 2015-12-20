package com.welflex.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.welflex.model.Flight;

public interface FlightDao {
  boolean decrementSeat(Long flightId, int quantity);

  List<Flight> findFlights(String fromAirportCode, String toAirportCode) throws DataAccessException;

  public List<Flight> getFlights();
  
  Flight getFlight(Long id);

  Flight getFlight(String flightNumber);

  void save(Flight flight);
}
