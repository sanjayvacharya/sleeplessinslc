package com.welflex.notes.rest;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.welflex.service.NotesHealthService;

@Path("/health")
public class HealthResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(HealthResource.class);

  private final NotesHealthService healthService;

  private final HttpServerRequest<ByteBuf> request;

  private final HttpServerResponse<ByteBuf> response;

  @SuppressWarnings("unchecked")
  @Inject
  public HealthResource(NotesHealthService healthService,
      @SuppressWarnings("rawtypes") HttpServerRequest request,
      @SuppressWarnings("rawtypes") HttpServerResponse response) {
    this.healthService = healthService;
    this.request = request;
    this.response = response;
  }

  public static final String INJECTED_RX_NETTY_REQUEST = "Injected RxNetty HttpServerRequest:";

  public static final String INJECTED_RX_NETTY_RESPONSE = "Injected RxNetty HttpServerResponse:";

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response isHealthy() {
    LOGGER.info("Received a Request for Health Check:" + request.getPath());

    if (healthService.isHealthy()) {
      StringBuilder s = new StringBuilder();
      s.append("STATUS:OK").append("\n").append(INJECTED_RX_NETTY_REQUEST).append(request != null)
          .append("\n").append(INJECTED_RX_NETTY_RESPONSE).append(response != null).append("\n");
      return Response.ok(s.toString(), MediaType.TEXT_PLAIN).build();
    }
    else {
      response.setStatus(HttpResponseStatus.SERVICE_UNAVAILABLE);
      response.writeString("Not OK");
      return Response.serverError().build();
    }
  }
}
