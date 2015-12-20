package com.welflex.notes;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

@Order(Ordered.HIGHEST_PRECEDENCE) 
public class NotesApplicationInitializer implements WebApplicationInitializer {

  private static final String SPRING_SECURITY_FILTER_NAME = "springSecurityFilterChain";
  
  @Override public void onStartup(ServletContext ctx) throws ServletException {
    // Listeners
    ctx.addListener(ContextLoaderListener.class);

    ctx.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM,
      AnnotationConfigWebApplicationContext.class.getName());
    ctx.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, SpringConfig.class.getName());

    // Register Spring Security Filter chain
    ctx.addFilter(SPRING_SECURITY_FILTER_NAME, DelegatingFilterProxy.class)
        .addMappingForUrlPatterns(
          EnumSet.<DispatcherType> of(DispatcherType.REQUEST, DispatcherType.FORWARD), false, "/*");

    // Register Jersey 2.0 servlet
    ServletRegistration.Dynamic servletRegistration = ctx.addServlet("Notes",
      ServletContainer.class.getName());
    
    servletRegistration.addMapping("/*");
    servletRegistration.setLoadOnStartup(1);
    servletRegistration.setInitParameter("javax.ws.rs.Application", NotesApplication.class.getName());
  }
}
