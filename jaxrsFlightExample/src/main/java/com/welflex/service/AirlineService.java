package com.welflex.service;

import java.util.List;

import com.welflex.model.Flight;
import com.welflex.model.FlightSearchCriteria;
import com.welflex.model.Flights;

public interface AirlineService {

  Flight getFlight(Long id) throws NoSuchFlightException;
  
  Flight getFlight(String flightNumber) throws NoSuchFlightException;

  List<Flight> getFlights();
  
  Flights getFlights(FlightSearchCriteria criteria);
}
