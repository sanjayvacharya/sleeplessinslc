package com.welflex.web.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.sun.jersey.api.view.Viewable;

@Path("/")
public class RootResource {
  @GET
  public Viewable getLoginResource() {
    return new Viewable("/home");
  }
  
  @Path("/home.html")
  @GET
  public Viewable getLogoutResource() {
    return new Viewable("/home");
  }
}
