package com.welflex.review.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ReviewExceptionMapper implements ExceptionMapper<RuntimeException>{

  @Override
  public Response toResponse(RuntimeException arg0) {
    arg0.printStackTrace();
    // TODO Auto-generated method stub
    return null;
  }

}
