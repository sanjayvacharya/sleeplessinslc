package com.welflex.rxnetty.jersey2;

import javax.ws.rs.core.Application;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.server.HttpServer;

public class RxNettyHttpServerFactory {

  private static final int DEFAULT_HTTP_PORT = 80;

  public static HttpServer<ByteBuf, ByteBuf> createHttpServer(Application application) {
    return createHttpServer(DEFAULT_HTTP_PORT, application, false);
  }

  public static HttpServer<ByteBuf, ByteBuf> createHttpServer(Application application, boolean start) {
    return createHttpServer(DEFAULT_HTTP_PORT, application, start);
  }

  public static HttpServer<ByteBuf, ByteBuf> createHttpServer(int port, Application application) {
    return createHttpServer(port, application, false);
  }

  public static HttpServer<ByteBuf, ByteBuf> createHttpServer(int port, Application application,
    boolean start) {
    HttpServer<ByteBuf, ByteBuf> httpServer = RxNetty.createHttpServer(port,
      new RxNettyHttpContainer(application));
    if (start) {
      httpServer.start();
    }
    return httpServer;
  }
}
