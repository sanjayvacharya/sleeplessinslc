package com.welflex.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.welflex.dao.FlightDao;
import com.welflex.model.Flight;
import com.welflex.model.FlightSearchCriteria;
import com.welflex.model.Flights;
import com.welflex.service.AirlineService;
import com.welflex.service.NoSuchFlightException;

@Service
@Transactional(readOnly = true)
public class AirlineServiceImpl implements AirlineService {

  private final FlightDao flightDao;

  public AirlineServiceImpl(FlightDao flightDao) {
    this.flightDao = flightDao;
  }
  
  @Override
  public List<Flight> getFlights() {
    return flightDao.getFlights();
  }

  @Override
  public Flight getFlight(Long id) throws NoSuchFlightException {
    Flight flight = flightDao.getFlight(id);
    if (flight != null) {
      return flight;
    }
    else {
      throw new NoSuchFlightException("Flight:" + id + " not found");
    }
  }

  @Override
  public Flights getFlights(FlightSearchCriteria criteria) {
    String fromAirportCode = criteria.getFromAirportCode();
    String toAirportCode = criteria.getToAirportCode();

    List<Flight> flights = flightDao.findFlights(fromAirportCode, toAirportCode);

    Flights results = new Flights();
    results.setFlights(flights);

    return results;
  }

  @Override
  public Flight getFlight(String flightNumber) throws NoSuchFlightException {
    Flight flight = flightDao.getFlight(flightNumber);
    if (flight != null) {
      return flight;
    }
    throw new NoSuchFlightException("Could not find flight:" + flightNumber);
  }

  @Override
  @Transactional(readOnly = false)
  public void save(Flight flight) {
    flightDao.save(flight);
  }
}
