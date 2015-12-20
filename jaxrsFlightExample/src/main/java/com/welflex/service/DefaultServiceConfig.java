package com.welflex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.welflex.dao.DaoConfig;

@Configuration
public class DefaultServiceConfig implements ServiceConfig {
  @Autowired
  private DaoConfig daoConfig;
  
  @Bean
  @Override
  public AirportService getAirportService() {
    return new AirportServiceImpl(daoConfig.getAirportDao());
  }
  
  @Bean
  @Override
  public AirlineService getAirlineService() {
    return new AirlineServiceImpl(daoConfig.getFlightDao());
  }
  
  @Bean
  @Override
  public ReservationService getReservationService() {
    return new ReservationServiceImpl(daoConfig.getFlightDao(), daoConfig.getTicketDao());
  }
}
