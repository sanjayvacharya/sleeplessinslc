package com.welflex.web.controller;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.welflex.model.Error;
import com.welflex.model.Flight;
import com.welflex.model.Reservation;
import com.welflex.model.Reservations;
import com.welflex.model.Ticket;
import com.welflex.service.AirlineService;
import com.welflex.service.NoSeatAvailableException;
import com.welflex.service.NoSuchFlightException;
import com.welflex.service.NoSuchReservationException;
import com.welflex.service.ReservationService;

@Controller
public class ReservationsController {
  private final AirlineService airlineService;

  private final ReservationService reservationService;

  public ReservationsController(ReservationService reservationService, AirlineService airlineService) {
    this.airlineService = airlineService;
    this.reservationService = reservationService;
  }

  @RequestMapping("/reservations.html")
  public ModelAndView getReservationsHtml() {
    List<Ticket> tickets = reservationService.getReservations();
    ModelAndView mav = new ModelAndView("reservations");
    mav.addObject("reservations", tickets);
    return mav;
  }
  
  @RequestMapping("/reservations/cancel/{reservationId}")
  public ModelAndView cancelReservation(@PathVariable Long reservationId) throws NoSuchReservationException {
    reservationService.cancelReservation(reservationId);
    return getReservationsHtml();
  }

  @RequestMapping(value = "/reservations", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  public Reservations getReservations() {
    return new Reservations(reservationService.getReservations());
  }

  @RequestMapping(value = "/reservations/{reservationId}", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  public Ticket getReservation(@PathVariable Long reservationId) throws NoSuchReservationException {
    return reservationService.getReservation(reservationId);
  }
  
  
  @ExceptionHandler(NoSuchReservationException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Error handleException(NoSuchReservationException e, final HttpServletRequest request, Writer writer) {  
    return new Error(e.getMessage());
  }

  @RequestMapping(value = "/reservations/{reservationId}.html", method = RequestMethod.GET)
  public String getReservationHtml(@PathVariable Long reservationId,
    Map<String, Object> model) {
    Ticket ticket = null;
    
    try {
      ticket = reservationService.getReservation(reservationId);
      model.put("ticket", ticket);
    }
    catch (NoSuchReservationException e) {
      model.put("message", e.getMessage());
    }
    
    return "confirmation";
  }

  @RequestMapping(value = "/bookFlight", method = RequestMethod.POST, produces = {
      MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, consumes = {
      MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  public Ticket bookFlight(@RequestBody Reservation reservation) throws NoSuchFlightException,
    NoSeatAvailableException {
    return reservationService.bookFlight(reservation);
  }

  @RequestMapping(value = "/bookFlight.html", method = RequestMethod.GET)
  public ModelAndView bookFlight(@RequestParam Long id) throws Exception {
    Flight flight = airlineService.getFlight(id);

    Reservation reservation = new Reservation();

    ModelAndView mav = new ModelAndView("bookFlight");
    mav.addObject("flight", flight);
    mav.addObject("reservation", reservation);

    return mav;
  }

  @RequestMapping(value = "/bookFlight.html", method = RequestMethod.POST)
  public String bookFlight(@Valid Reservation booking, BindingResult result,
    Map<String, Object> model) throws Exception {
    Flight flight = airlineService.getFlight(booking.getFlightId());

    if (result.hasErrors()) {
      model.put("flight", flight);
      return "bookFlight";
    }
    else {
      try {
        Ticket ticket = reservationService.bookFlight(booking);

        model.put("ticket", ticket);

        return "confirmation";
      }
      catch (NoSuchFlightException e) {
        model.put("message", e.getMessage());
      }
      catch (NoSeatAvailableException e) {
        model.put("message", e.getMessage());
      }

      model.put("flight", flight);

      return "bookFlight";
    }
  }
}
