package com.welflex.web.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.sun.jersey.api.view.Viewable;

@Path("logoutSuccess.html")
public class LogoutResource {
  
  @GET
  public Viewable getLogoutResource() {
    return new Viewable("/logoutSuccess");
  }
}
