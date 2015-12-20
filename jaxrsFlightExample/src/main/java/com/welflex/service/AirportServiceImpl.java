package com.welflex.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.welflex.dao.AirportDao;
import com.welflex.model.Airport;

@Service
@Transactional(readOnly = true)
public class AirportServiceImpl implements AirportService {
  private final AirportDao airportDao;

  public AirportServiceImpl(AirportDao airportDao) {
    this.airportDao = airportDao;
  }

  @Override
  public List<Airport> getAirports() {
    return airportDao.getAirports();
  }

  @Override
  public Airport getAirport(String code) throws NoSuchAirportException {
    Airport airport = airportDao.getAirport(code);
    if (airport != null) {
      return airport;
    }
    throw new NoSuchAirportException("Airport:" + code + " not found");
  }

  @Override
  @Transactional(readOnly=false)
  public void create(Airport airport) {
    airportDao.save(airport);
  }
}
