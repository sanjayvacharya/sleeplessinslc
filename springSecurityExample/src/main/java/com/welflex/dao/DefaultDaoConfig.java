package com.welflex.dao;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.joda.time.DateTime;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.welflex.model.Airport;
import com.welflex.model.Flight;
import com.welflex.model.Ticket;

@Configuration
@EnableTransactionManagement
public class DefaultDaoConfig implements DaoConfig, TransactionManagementConfigurer, InitializingBean {
  
  @Bean
  @Override
  public FlightDao getFlightDao() {
    return new FlightDaoImpl(getSessionFactory());
  }

  @Bean
  @Override
  public TicketDao getTicketDao() {
    return new TicketDaoImpl(getSessionFactory());
  }
  
  @Bean
  @Override
  public AirportDao getAirportDao() {
    return new AirportDaoImpl(getSessionFactory());
  }
  
  @Bean
  public SessionFactory getSessionFactory() {
    return new AnnotationConfiguration().addAnnotatedClass(Ticket.class)
      .addAnnotatedClass(Flight.class).addAnnotatedClass(Airport.class)
      .configure().buildSessionFactory();
  }

  @Override
  public PlatformTransactionManager annotationDrivenTransactionManager() {
    return new HibernateTransactionManager(getSessionFactory());
  }
  
  @Override
  public void afterPropertiesSet() throws Exception {
    // Initializes the Database
    Airport lax = new Airport("LAX", "Los Angeles International", "Los Angeles");
    getAirportDao().save(lax);
    Airport jfk = new Airport("JFK", "JFK International", "New York");
    getAirportDao().save(jfk);
    Airport ord = new Airport("ORD", "Chicago O'Hare", "Chicago");
    getAirportDao().save(ord);

    getFlightDao().save(createFlight("LH 235", lax, ord, 8, 1800, 120));
    getFlightDao().save(createFlight("LH 123", ord, lax, 8, 1800, 120));
    getFlightDao().save(createFlight("AA 222", jfk, ord, 4, 590, 200));
    getFlightDao().save(createFlight("AA 112", lax, ord, 8, 1800, 120));
    getFlightDao().save(createFlight("AA 231", lax, jfk, 8, 2200, 120));
    getFlightDao().save(createFlight("UA 112", jfk, ord, 4, 590, 120));
    getFlightDao().save(createFlight("AA 232", jfk, lax, 8, 2200, 120));
    getFlightDao().save(createFlight("LH 237", ord, lax, 8, 1800, 120));
  }
  
  private Flight createFlight(String number, Airport departure, Airport arrival, int flyinghrs,
    int miles, int seatsAvail) {
    Flight f = new Flight();
    f.setFrom(departure);
    f.setTo(arrival);

    DateTime departureTime = new DateTime(new Date()).plusDays(1);
    DateTime arrivalTime = departureTime.plusHours(flyinghrs);
    f.setDepartureTime(departureTime);
    f.setArrivalTime(arrivalTime);
    f.setNumber(number);
    f.setSeatsAvailable(seatsAvail);
    f.setMiles(miles);

    return f;
  }
}
