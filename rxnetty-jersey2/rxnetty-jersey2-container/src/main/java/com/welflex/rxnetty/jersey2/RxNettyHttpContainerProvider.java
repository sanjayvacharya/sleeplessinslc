package com.welflex.rxnetty.jersey2;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.spi.ContainerProvider;

public class RxNettyHttpContainerProvider implements ContainerProvider {
  
  @Override
  public <T> T createContainer(Class<T> type, Application application) throws ProcessingException {
    if (!type.equals(RxNettyHttpContainer.class)) {
      throw new IllegalArgumentException("RxNettyHttpContainerProvider can only create a Container of Type:" + RxNettyHttpContainer.class.getName()
        + ". Invoked with :" + type.getName());
    }
    return type.cast(new RxNettyHttpContainer(application));
  }
}
