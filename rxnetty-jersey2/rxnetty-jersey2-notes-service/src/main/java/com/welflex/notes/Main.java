package com.welflex.notes;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.welflex.notes.rest.NotesApplication;
import com.welflex.rxnetty.jersey2.RxNettyHttpServerFactory;

/**
 * A Launcher application for RxNetty container using Spring for dependency injection
 */
public class Main {

  static final String CONTEX_CONFIG = "contextConfig";
  
  
  public static void main(String args[]) {
    ResourceConfig resourceConfig = ResourceConfig.forApplication(new NotesApplication());
    AnnotationConfigApplicationContext ctx = null;
    
    try {
      ctx = new AnnotationConfigApplicationContext();
      ctx.register(NotesSpringConfiguration.class);
      ctx.refresh();
      resourceConfig.property(CONTEX_CONFIG, ctx);
      RxNettyHttpServerFactory.createHttpServer(9182, resourceConfig, false).startAndWait();
    }
    finally {
      IOUtils.closeQuietly(ctx);
    }
  }
}
