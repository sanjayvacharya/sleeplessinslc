package com.welflex.web.resource;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.common.collect.Maps;
import com.sun.jersey.api.view.Viewable;
import com.welflex.model.Ticket;
import com.welflex.service.NoSuchReservationException;
import com.welflex.service.ReservationService;

@Path("/reservations/{reservationId}.html")
public class ReservationResource {
  private final ReservationService reservationService;

  public ReservationResource(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GET
  public Viewable getReservationHtml(@PathParam("reservationId") Long reservationId) {
    Ticket ticket = null;
    Map<String, Object> model = Maps.newHashMap();
    try {
      ticket = reservationService.getReservation(reservationId);
      model.put("ticket", ticket);
    }
    catch (NoSuchReservationException e) {
      model.put("message", e.getMessage());
    }

    return new Viewable("/confirmation", model);
  }
}
