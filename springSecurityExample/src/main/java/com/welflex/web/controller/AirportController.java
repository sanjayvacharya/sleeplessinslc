package com.welflex.web.controller;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.welflex.model.Airport;
import com.welflex.model.Airports;
import com.welflex.model.Error;
import com.welflex.service.AirportService;
import com.welflex.service.NoSuchAirportException;

@Controller
public class AirportController {
  private final AirportService airportService;

  public AirportController(AirportService airportService) {
    this.airportService = airportService;
  }
  
  @RequestMapping(value = "/airports.html", method = RequestMethod.GET)
  public ModelAndView getAirportsHtml() {
    ModelAndView mav = new ModelAndView("airports");
    mav.addObject("airports", airportService.getAirports());   
    mav.addObject("airport", new Airport());
    return mav;
  }
  
  @RequestMapping(value = "/airports.html", method=RequestMethod.POST)
  public String createAirport(@Valid Airport airport, BindingResult result,
    Map<String, Object> model)  {
   
    if (result.hasErrors()) {
      model.put("airports", airportService.getAirports());   
      return "airports";
    }
    
    airportService.create(airport);
    return "redirect:/airports.html";
  }
  
  @RequestMapping(value="/deleteAirport.html")
  public ModelAndView deleteAirports(@RequestParam(required = true) String code) {
    airportService.delete(code);
    return getAirportsHtml();
  }

  @RequestMapping(value = "/airports", method = RequestMethod.GET, produces = {
      MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  public Airports getAirports() {
    return new Airports(airportService.getAirports());
  }

  @RequestMapping(value = "/airports/{code}", method = RequestMethod.GET,  produces = {
      MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
  @ResponseBody
  public Airport getAirport(@PathVariable String code) throws NoSuchAirportException {
    return airportService.getAirport(code);
  }
  
  @ExceptionHandler(NoSuchAirportException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Error handleException(NoSuchAirportException e, final HttpServletRequest request, Writer writer) {  
    return new Error(e.getMessage());
  }
}
