package com.welflex.notes.spring.security;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.security.access.AccessDeniedException;

public class AccessDeniedExeptionMapper implements ExceptionMapper<AccessDeniedException> {

  @Override
  public Response toResponse(AccessDeniedException exception) {
    return Response.status(Status.FORBIDDEN).entity(exception.getMessage()).build();
  }
}
