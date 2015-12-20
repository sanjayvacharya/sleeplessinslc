package com.welflex.web.resource;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.google.common.collect.Maps;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.api.view.Viewable;
import com.welflex.model.Airport;
import com.welflex.model.FlightSearchCriteria;
import com.welflex.model.Flights;
import com.welflex.service.AirlineService;
import com.welflex.service.AirportService;

@Path("/searchFlights.html")
public class FlightsResource {

  private final AirlineService airlineService;
  private final AirportService airportService;

  public FlightsResource(AirlineService airlineService, AirportService airportService) {
    this.airlineService = airlineService;
    this.airportService = airportService;
  }

  @GET
  public Viewable getFlights() throws Exception {
    Map<String, List<Airport>> airports = Maps.newHashMap();
    airports.put("airports", airportService.getAirports());
    
    return new Viewable("/searchFlights", airports);
  }
  
  @POST
  public Viewable searchFlights(@InjectParam FlightSearchCriteria criteria) throws Exception {
    Map<String, Object> modelMap = Maps.newHashMap();
    
    modelMap.put("airports", airportService.getAirports());
    modelMap.put("from", criteria.getFromAirportCode());
    modelMap.put("to", criteria.getToAirportCode());

    Flights searchResult = airlineService.getFlights(criteria);

    modelMap.put("flightSearchResult", searchResult);

    return new Viewable("/searchFlights", modelMap);
  }
}
