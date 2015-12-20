package com.welflex.notes.rest;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

@Provider
@Loggable
public class LoggingFilter implements ContainerRequestFilter {
  private static final Logger LOG = Logger.getLogger(LoggingFilter.class);

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    MultivaluedMap<String, String> map = requestContext.getHeaders();
    LOG.info(map);
  }
}
