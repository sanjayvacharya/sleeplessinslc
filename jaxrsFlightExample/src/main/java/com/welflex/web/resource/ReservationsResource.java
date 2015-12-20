package com.welflex.web.resource;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.google.common.collect.Maps;
import com.sun.jersey.api.view.Viewable;
import com.welflex.model.Ticket;
import com.welflex.service.ReservationService;

@Path("/reservations.html")
public class ReservationsResource {

  private final ReservationService reservationService;

  public ReservationsResource(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GET
  public Viewable getReservations() {
    Map<String, List<Ticket>> modelMap = Maps.newHashMap();
    modelMap.put("reservations", reservationService.getReservations());

    return new Viewable("/reservations", modelMap);
  }
}
