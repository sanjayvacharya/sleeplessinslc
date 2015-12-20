package com.welflex.web.resource;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Maps;
import com.sun.jersey.api.view.Viewable;

@Path("/login.html")
public class LoginResource {

  @GET
  public Viewable getLoginResource(@QueryParam("login_error") String login_error) {
    if (StringUtils.isNotBlank(login_error)) {
      Map<String, Object> modelMap = Maps.newHashMap();
      modelMap.put("errors", "true");
      return new Viewable("/login", modelMap);
    }
    
    return new Viewable("/login");
  }
}
