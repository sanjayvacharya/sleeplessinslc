package com.welflex.service;

import java.util.List;

import com.welflex.model.Airport;

public interface AirportService {
  
  void create(Airport airport);
  
  List<Airport> getAirports();
  
  Airport getAirport(String code) throws NoSuchAirportException;
}
