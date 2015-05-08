package com.welflex.rxnetty.jersey2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpResponseHeaders;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.SecurityContext;

import netflix.karyon.transport.util.HttpContentInputStream;

import org.apache.commons.io.IOUtils;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.internal.MapPropertiesDelegate;
import org.glassfish.jersey.internal.inject.ReferencingFactory;
import org.glassfish.jersey.internal.util.collection.Ref;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ApplicationHandler;
import org.glassfish.jersey.server.ContainerException;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.internal.ConfigHelper;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.glassfish.jersey.server.spi.ContainerResponseWriter;
import org.glassfish.jersey.server.spi.RequestScopedInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rx.Observable;
import rx.schedulers.Schedulers;

public class RxNettyHttpContainer implements Container, RequestHandler<ByteBuf, ByteBuf> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RxNettyHttpContainer.class);

  private volatile ApplicationHandler appHandler;

  private final Type RequestTYPE = (new TypeLiteral<Ref<HttpServerRequest<ByteBuf>>>() {})
      .getType();

  private final Type ResponseTYPE = (new TypeLiteral<Ref<HttpServerResponse<ByteBuf>>>() {})
      .getType();

  private volatile ContainerLifecycleListener containerListener;

  /**
   * Referencing factory for RxNetty HttpServerRequest.
   */
  private static class RxNettyRequestReferencingFactory extends
      ReferencingFactory<HttpServerRequest<ByteBuf>> {

    @Inject
    public RxNettyRequestReferencingFactory(
        final Provider<Ref<HttpServerRequest<ByteBuf>>> referenceFactory) {
      super(referenceFactory);
    }
  }

  /**
   * Referencing factory for RxNetty HttpServerResponse.
   */
  private static class RxNettyResponseReferencingFactory extends
      ReferencingFactory<HttpServerResponse<ByteBuf>> {

    @Inject
    public RxNettyResponseReferencingFactory(
        final Provider<Ref<HttpServerResponse<ByteBuf>>> referenceFactory) {
      super(referenceFactory);
    }
  }

  /**
   * An internal binder to enable RxNetty HTTP container specific types injection. This binder
   * allows to inject underlying RxNetty HTTP request and response instances. Note that since
   * RxNetty {@code HttpServerRequest} class is not proxiable as it will be declared packaged only
   */
  static class RxNettyBinder extends AbstractBinder {

    @Override
    protected void configure() {
      bindFactory(RxNettyRequestReferencingFactory.class).to(HttpServerRequest.class).proxy(false)
          .in(RequestScoped.class);
      bindFactory(ReferencingFactory.<HttpServerRequest<ByteBuf>> referenceFactory()).to(
        new TypeLiteral<Ref<HttpServerRequest<ByteBuf>>>() {}).in(RequestScoped.class);

      bindFactory(RxNettyResponseReferencingFactory.class).to(HttpServerResponse.class).proxy(true)
          .proxyForSameScope(false).in(RequestScoped.class);
      bindFactory(ReferencingFactory.<HttpServerResponse<ByteBuf>> referenceFactory()).to(
        new TypeLiteral<Ref<HttpServerResponse<ByteBuf>>>() {}).in(RequestScoped.class);
    }
  }

  public RxNettyHttpContainer(Application application) {
    this.appHandler = new ApplicationHandler(application, new RxNettyBinder());
    this.containerListener = ConfigHelper.getContainerLifecycleListener(appHandler);
  }

  @Override
  public ApplicationHandler getApplicationHandler() {
    return appHandler;
  }

  @Override
  public ResourceConfig getConfiguration() {
    return appHandler.getConfiguration();
  }

  @Override
  public void reload() {
    reload(appHandler.getConfiguration());
  }

  @Override
  public void reload(ResourceConfig configuration) {
    containerListener.onShutdown(this);
    appHandler = new ApplicationHandler(configuration, new RxNettyBinder());
    containerListener = ConfigHelper.getContainerLifecycleListener(appHandler);
    containerListener.onReload(this);
    containerListener.onStartup(this);
  }

  private URI toUri(String uri) throws IllegalArgumentException {
    try {
      return new URI(uri);
    }
    catch (URISyntaxException e) {
      LOGGER.error(String.format("Invalid URI:%s", uri), e);
      throw new IllegalArgumentException("Invalid URI:" + uri, e);
    }
  }

  public Observable<Void> handle(HttpServerRequest<ByteBuf> request,
    HttpServerResponse<ByteBuf> response) {
    InputStream requestData = new HttpContentInputStream(response.getAllocator(),
        request.getContent());
    URI baseUri = toUri("/");
    URI requestUri = toUri(request.getUri());

    ContainerRequest containerRequest = new ContainerRequest(baseUri, requestUri, request
        .getHttpMethod().name(), getSecurityContext(requestUri, request),
        new MapPropertiesDelegate());

    containerRequest.setEntityStream(requestData);

    for (String headerName : request.getHeaders().names()) {
      containerRequest.headers(headerName, request.getHeaders().getAll(headerName));
    }

    containerRequest.setWriter(new RxNettyContainerResponseWriter(response));

    containerRequest.setRequestScopedInitializer(new RequestScopedInitializer() {

      @Override
      public void initialize(final ServiceLocator locator) {
        locator.<Ref<HttpServerRequest<ByteBuf>>> getService(RequestTYPE).set(request);
        locator.<Ref<HttpServerResponse<ByteBuf>>> getService(ResponseTYPE).set(response);
      }
    });
    return Observable.<Void> create(subscriber -> {
      try {
        appHandler.handle(containerRequest);
        subscriber.onCompleted();
      }
      finally {
        IOUtils.closeQuietly(requestData);
      }
    }).doOnTerminate(() -> response.close(true)).subscribeOn(Schedulers.io());
  }

  private SecurityContext getSecurityContext(URI requestUri, HttpServerRequest<ByteBuf> request) {
    return new SecurityContext() {

      @Override
      public boolean isUserInRole(String role) {
        return false;
      }

      @Override
      public boolean isSecure() {
        return requestUri.getScheme().equalsIgnoreCase("https");
      }

      @Override
      public Principal getUserPrincipal() {
        return null;
      }

      @Override
      public String getAuthenticationScheme() {
        return null;
      }
    };
  }

  public static class RxNettyContainerResponseWriter implements ContainerResponseWriter {
    private static final Logger LOGGER = LoggerFactory
        .getLogger(RxNettyContainerResponseWriter.class);

    private final HttpServerResponse<ByteBuf> serverResponse;

    private final ByteBuf contentBuffer;

    public RxNettyContainerResponseWriter(HttpServerResponse<ByteBuf> serverResponse) {
      this.serverResponse = serverResponse;
      this.contentBuffer = serverResponse.getChannel().alloc().buffer();
    }

    @Override
    public void commit() {
      if (!serverResponse.isCloseIssued()) {
        serverResponse.writeAndFlush(contentBuffer);
      }
    }

    @Override
    public boolean enableResponseBuffering() {
      return true;
    }

    @Override
    public void failure(Throwable throwable) {
      LOGGER.error("Failure Servicing Request", throwable);
      if (!serverResponse.isCloseIssued()) {
        serverResponse.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        serverResponse.writeString("Request Failed...");
      }
    }

    @Override
    public void setSuspendTimeout(long arg0, TimeUnit arg1) throws IllegalStateException {}

    @Override
    public boolean suspend(long arg0, TimeUnit arg1, TimeoutHandler arg2) {
      return false;
    }

    @Override
    public OutputStream writeResponseStatusAndHeaders(long contentLength,
      ContainerResponse containerResponse) throws ContainerException {

      serverResponse.setStatus(HttpResponseStatus.valueOf(containerResponse.getStatus()));
      HttpResponseHeaders responseHeaders = serverResponse.getHeaders();

      if (!containerResponse.getHeaders().containsKey(HttpHeaders.Names.CONTENT_LENGTH)) {
        responseHeaders.setHeader(HttpHeaders.Names.CONTENT_LENGTH, contentLength);
      }

      for (Map.Entry<String, List<String>> header : containerResponse.getStringHeaders().entrySet()) {
        responseHeaders.setHeader(header.getKey(), header.getValue());
      }

      return new ByteBufOutputStream(contentBuffer);
    }
  }
}
