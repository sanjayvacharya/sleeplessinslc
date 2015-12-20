package com.welflex.notes.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.welflex.notes.exception.NoteNotFoundException;

public class NoteNotFoundExceptionMapper implements ExceptionMapper<NoteNotFoundException> {
  @Override
  public Response toResponse(NoteNotFoundException exception) {
    return Response.status(Status.NOT_FOUND.getStatusCode()).entity(exception.getMessage()).build();
  }
}
