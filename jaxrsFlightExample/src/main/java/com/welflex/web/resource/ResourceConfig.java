package com.welflex.web.resource;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.welflex.service.ServiceConfig;

@Configuration
public class ResourceConfig {
  
  @Autowired
  private ServiceConfig serviceConfig;
  
  @Bean
  public Validator getValidator() {
    return new LocalValidatorFactoryBean();
  }

  @Bean
  public LoginResource getLoginResource() {
    return new LoginResource();
  }
  
  @Bean
  public LogoutResource getLogoutResource() {
    return new LogoutResource();
  }
  
  @Bean
  public RootResource getRootResource() {
    return new RootResource();
  }
  
  @Bean
  public AirportResource getAirportResource() {
    return new AirportResource(serviceConfig.getAirportService(), getValidator());
  }
  
  @Bean
  public FlightsResource getFlightsResource() {
    return new FlightsResource(serviceConfig.getAirlineService(), serviceConfig.getAirportService());
  }
  
  @Bean
  public FlightBookResource getFlightBookResource() {
    return new FlightBookResource(serviceConfig.getAirlineService(), serviceConfig.getReservationService(),
      getValidator());
  }
  
  @Bean
  public ReservationsResource getReservationsResource() {
    return new ReservationsResource(serviceConfig.getReservationService());
  }
  
  @Bean
  public ReservationResource getReservationResource() {
    return new ReservationResource(serviceConfig.getReservationService());
  }
  
}
