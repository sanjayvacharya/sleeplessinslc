package com.welflex.product;

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

@Order(Ordered.HIGHEST_PRECEDENCE) 
public class ProductApplicationInitializer implements WebApplicationInitializer {
  
  @Override public void onStartup(ServletContext ctx) throws ServletException {
    // Listeners
    ctx.addListener(ContextLoaderListener.class);

    ctx.setInitParameter(ContextLoader.CONTEXT_CLASS_PARAM,
      AnnotationConfigWebApplicationContext.class.getName());
    ctx.setInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, ProductJavaConfig.class.getName());

    // Register Jersey 2.0 servlet
    ServletRegistration.Dynamic servletRegistration = ctx.addServlet("Product",
      ServletContainer.class.getName());
    
    servletRegistration.addMapping("/*");
    servletRegistration.setLoadOnStartup(1);
    servletRegistration.setInitParameter("javax.ws.rs.Application", ProductApplication.class.getName());
  }
}
