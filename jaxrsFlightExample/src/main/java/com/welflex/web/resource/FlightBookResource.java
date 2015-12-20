package com.welflex.web.resource;

import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import com.google.common.collect.Maps;
import com.sun.jersey.api.view.Viewable;
import com.welflex.model.Flight;
import com.welflex.model.Reservation;
import com.welflex.model.Ticket;
import com.welflex.service.AirlineService;
import com.welflex.service.NoSuchFlightException;
import com.welflex.service.ReservationService;

@Path("/bookFlight.html")
public class FlightBookResource {
  private final AirlineService airlineService;

  private final ReservationService reservationService;

  private final Validator validator;

  public FlightBookResource(AirlineService airlineService, ReservationService reservationService,
      Validator validator) {
    this.airlineService = airlineService;
    this.reservationService = reservationService;
    this.validator = validator;
  }

  @GET
  public Viewable getBookFlight(@QueryParam("id") Long flightId) throws NoSuchFlightException {
    Map<String, Object> modelMap = Maps.newHashMap();
    modelMap.put("flight", airlineService.getFlight(flightId));
    modelMap.put("reservation", new Reservation());

    return new Viewable("/bookFlight", modelMap);
  }

  @POST
  public Viewable bookFlight(@FormParam("reservationName") String reservationName,
    @FormParam("quantity") int quantity, @FormParam("flightId") Long flightId) throws NoSuchFlightException {
    Flight flight = airlineService.getFlight(flightId);
    Reservation r = new Reservation(reservationName, quantity, flightId);

    Map<String, Object> modelMap = Maps.newHashMap();
    modelMap.put("flight", flight);

    Set<ConstraintViolation<Reservation>> violations = validator.validate(r);

    if (violations.size() > 0) {
      modelMap.put("errors", violations);
    }
    else {
      try {
        Ticket ticket = reservationService.bookFlight(r);
        modelMap.put("ticket", ticket);

        return new Viewable("/confirmation", modelMap);
      }
      catch (Exception e) {
        modelMap.put("message", e.getMessage());
      }
    }

    return new Viewable("/bookFlight", modelMap);
  }
}
