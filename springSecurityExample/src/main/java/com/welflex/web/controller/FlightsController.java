package com.welflex.web.controller;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.welflex.model.Airport;
import com.welflex.model.Error;
import com.welflex.model.Flight;
import com.welflex.model.FlightSearchCriteria;
import com.welflex.model.FlightTransferObject;
import com.welflex.model.Flights;
import com.welflex.service.AirlineService;
import com.welflex.service.AirportService;
import com.welflex.service.NoSuchAirportException;
import com.welflex.service.NoSuchFlightException;

@Controller
public class FlightsController {

  private final AirlineService airlineService;

  private final AirportService airportService;

  public FlightsController(AirlineService airlineService, AirportService airportService) {
    this.airlineService = airlineService;
    this.airportService = airportService;
  }

  @RequestMapping(value = "/flightForm.html", method = RequestMethod.GET)
  public ModelAndView manageFlights(@RequestParam(required = false) Long flightId) throws NoSuchFlightException {
    List<Airport> airports = airportService.getAirports();

    ModelAndView mav = new ModelAndView("flightForm");

    mav.addObject("airports", airports);
    mav.addObject("flight", create(flightId != null
        ? airlineService.getFlight(flightId)
        : new Flight()));

    return mav;
  }

  @RequestMapping(value = "/flightForm.html", method = RequestMethod.POST)
  public ModelAndView saveFlight(@ModelAttribute("flight") @Valid FlightTransferObject flightDto,
    BindingResult result, Map<String, Object> modelMap, HttpServletRequest request) throws Exception {

    if (request.getParameter("cancel") != null) {
      return searchFlights();
    }

    if (result.hasErrors()) {
      ModelAndView mav = new ModelAndView("flightForm");

      mav.addAllObjects(modelMap);
      mav.addObject("airports", airportService.getAirports());

      return mav;
    }

    Flight flight = create(flightDto);

    airlineService.save(flight);

    FlightSearchCriteria criteria = new FlightSearchCriteria();
    criteria.setFromAirportCode(flightDto.getFromAirportCode());
    criteria.setToAirportCode(flightDto.getToAirportCode());

    return searchFlights(criteria);
  }

  private Flight create(FlightTransferObject flightDto) throws NoSuchAirportException,
    NoSuchFlightException {
    Flight flight = flightDto.getId() != null
        ? airlineService.getFlight(flightDto.getId())
        : new Flight();
    flight.setNumber(flightDto.getNumber());
    String hrMin[] = StringUtils.split(flightDto.getDepartureTime(), ':');
    
    DateTime dateTime = new DateTime(flightDto.getDepartureDate()).withHourOfDay(Integer.valueOf(hrMin[0]))
        .withMinuteOfHour(Integer.valueOf(hrMin[1]));
  
    flight.setDepartureTime(dateTime);
  
    hrMin = StringUtils.split(flightDto.getArrivalTime(), ":");
    dateTime = new DateTime(flightDto.getArrivalDate()).withHourOfDay(Integer.valueOf(hrMin[0]))
        .withMinuteOfHour(Integer.valueOf(hrMin[1]));
    
    flight.setArrivalTime(dateTime);
    flight.setMiles(flightDto.getMiles());
    flight.setSeatsAvailable(flightDto.getNumberOfSeats());

    flight.setFrom(airportService.getAirport(flightDto.getFromAirportCode()));
    flight.setTo(airportService.getAirport(flightDto.getToAirportCode()));

    return flight;
  }

  private FlightTransferObject create(Flight flight) {
    FlightTransferObject transferObject = new FlightTransferObject();
    transferObject.setNumber(flight.getNumber());

    if (flight.getArrivalTime() != null) {
      DateTime arrivalTime = flight.getArrivalTime();

      transferObject.setArrivalDate(arrivalTime.toDate());
      transferObject.setArrivalTime(arrivalTime.getHourOfDay() + ":"
        + arrivalTime.getMinuteOfHour());
    }

    if (flight.getDepartureTime() != null) {
      DateTime departureTime = flight.getDepartureTime();

      transferObject.setDepartureDate(departureTime.toDate());
      transferObject.setDepartureTime(departureTime.getHourOfDay() + ":"
        + departureTime.getMinuteOfHour());
    }
    
    transferObject.setFromAirportCode(flight.getFrom() == null
        ? ""
        : flight.getFrom().getCode());
    transferObject.setToAirportCode(flight.getTo() == null
        ? ""
        : flight.getTo().getCode());

    transferObject.setNumberOfSeats(flight.getSeatsAvailable());
    transferObject.setMiles(flight.getMiles());

    return transferObject;
  }

  @RequestMapping(value = "/searchFlights.html", method = RequestMethod.GET)
  public ModelAndView searchFlights() throws Exception {
    List<Airport> airports = airportService.getAirports();

    ModelAndView mav = new ModelAndView("searchFlights");

    mav.addObject("airports", airports);

    return mav;
  }

  @RequestMapping(value = "/searchFlights.html", method = RequestMethod.POST)
  public ModelAndView searchFlights(FlightSearchCriteria criteria) throws Exception {

    ModelAndView mav = new ModelAndView("searchFlights");

    mav.addObject("airports", airportService.getAirports());

    mav.addObject("from", criteria.getFromAirportCode());
    mav.addObject("to", criteria.getToAirportCode());

    Flights searchResult = airlineService.getFlights(criteria);

    mav.addObject("flightSearchResult", searchResult);

    return mav;
  }

  @RequestMapping(value = "/searchFlights", method = RequestMethod.POST, consumes = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  @ResponseBody
  public Flights flightSearch(@RequestBody FlightSearchCriteria criteria) {
    return airlineService.getFlights(criteria);
  }

  @RequestMapping(value = "/flights", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  @ResponseBody
  public Flights getFlights() {
    return new Flights(airlineService.getFlights());
  }

  @RequestMapping(value = "/flights/{flightNumber}", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  @ResponseBody
  public Flight getFlightDetail(@PathVariable String flightNumber) throws NoSuchFlightException {
    return airlineService.getFlight(flightNumber);
  }

  @ExceptionHandler(NoSuchFlightException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Error handleException(NoSuchFlightException e, final HttpServletRequest request,
    Writer writer) {
    return new Error(e.getMessage());
  }
}
