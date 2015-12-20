package com.welflex.dao;

import java.util.List;

import com.welflex.model.Airport;

public interface AirportDao {
  Airport getAirport(String code);

  void save(Airport airport);

  List<Airport> getAirports();
}
