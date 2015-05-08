package com.welflex.notes;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.welflex.notes.rest.NotesApplication;

public class GrizzlyServerLauncher {
  private static final int PORT = 8180;
  
  public static void main(String args[]) throws URISyntaxException, IOException {
    ResourceConfig resourceConfig = ResourceConfig.forApplication(new NotesApplication());
    AnnotationConfigApplicationContext ctx = null;
    try {
      ctx = new AnnotationConfigApplicationContext();
      ctx.register(NotesSpringConfiguration.class);
      ctx.refresh();
      resourceConfig.property("contextConfig", ctx);
     
      HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(new URI("http://localhost:" + PORT),
        resourceConfig, true);
      System.in.read();
      httpServer.shutdown();
    }
    finally {
      IOUtils.closeQuietly(ctx);
    }
  }
}
