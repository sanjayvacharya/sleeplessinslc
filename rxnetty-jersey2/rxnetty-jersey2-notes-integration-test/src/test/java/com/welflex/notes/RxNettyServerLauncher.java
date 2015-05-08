package com.welflex.notes;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.server.HttpServer;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.welflex.notes.rest.NotesApplication;
import com.welflex.rxnetty.jersey2.RxNettyHttpServerFactory;

public class RxNettyServerLauncher {
  
  private static final int PORT = 8181;
  
  public static void main(String args[]) throws IOException, InterruptedException {
    ResourceConfig resourceConfig = ResourceConfig.forApplication(new NotesApplication());
    AnnotationConfigApplicationContext ctx = null;
    try {
      ctx = new AnnotationConfigApplicationContext();
      ctx.register(NotesSpringConfiguration.class);
      ctx.refresh();
      resourceConfig.property("contextConfig", ctx);
      HttpServer<ByteBuf, ByteBuf> nettyHttpServer = RxNettyHttpServerFactory.createHttpServer(PORT, resourceConfig, true);
      System.in.read();
      nettyHttpServer.shutdown();
    }
    finally {
      IOUtils.closeQuietly(ctx);
    }
  }
}
