package com.welflex.jms.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.welflex.jms.annotations.DestinationJndi;
import com.welflex.jms.annotations.MessageType;

public class MessageSendProxy implements InvocationHandler {
  private final MessageSendExecutor executor;
  private final Class<?> proxyFor;

  public MessageSendProxy(MessageSendExecutor executor, Class<?> proxyFor) {
    this.executor = executor;
    this.proxyFor = proxyFor;
  }

  private static boolean isSendMethod(Method method) {
    return method.isAnnotationPresent(DestinationJndi.class);
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (method.getName().equals("equals")) {
      return this.equals(proxy);
    }
    else if (method.getName().equals("hashCode")) {
      return this.hashCode();
    }
    else if (method.getName().equals("toString")) {
      return this.toString();
    }
    else if (isSendMethod(method)) {
      MessageSendRequest request = new MessageSendRequest();
      DestinationJndi destJndi = method.getAnnotation(DestinationJndi.class);
      request.setDestination(destJndi.value());
      request.setPayload(args[0]);
      MessageType msgType = method.getAnnotation(MessageType.class);
      request.setMessageType(msgType.value());
      executor.send(request);
      return null;
    }
    throw new IllegalStateException("Proxy cannot support non-send methods");
  }

  @Override
  public String toString() {
    return "Proxy class for:" + proxyFor;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof MessageSendProxy))
      return false;
    MessageSendProxy other = (MessageSendProxy) obj;
    if (other == this)
      return true;

    if (other.proxyFor != this.proxyFor)
      return false;
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return proxyFor.hashCode();
  }

}
