package com.welflex.web.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.collect.Maps;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.api.view.Viewable;
import com.welflex.model.Airport;
import com.welflex.service.AirportService;

@Path("/airports.html")
public class AirportResource {
  private final AirportService airportService;
  private final Validator validator;

  public AirportResource(AirportService airportService, Validator validator) {
    this.airportService = airportService;
    this.validator = validator;
  }
  
  @GET
  public Viewable getAirports() {
    Map<String, Object> modelMap = Maps.newHashMap();
    modelMap.put("airports", airportService.getAirports());
    Airport airport = new Airport();
    modelMap.put("airport", airport);
    return new Viewable("/airports", modelMap);
  }
  
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public Response createAirport(Form form) throws URISyntaxException  {
    Airport airport = new Airport(form.getFirst("code"), form.getFirst("name"), 
      form.getFirst("city"));
    
    Set<ConstraintViolation<Airport>> violations = validator.validate(airport);
    
    if (violations.size() > 0) {
      
      Viewable viewable = getAirports();
      
      @SuppressWarnings("unchecked")
      Map<String, Object> modelMap = (Map<String, Object>) viewable.getModel();
      modelMap.put("errors", violations);
      modelMap.put("airport", airport);
    
      return Response.ok(viewable).build();
    }
    
    airportService.create(airport);
    
    return Response.seeOther(new URI("/airports.html")).build();
  }
}
