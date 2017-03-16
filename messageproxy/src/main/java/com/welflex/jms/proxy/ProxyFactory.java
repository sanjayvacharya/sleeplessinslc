package com.welflex.jms.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.jms.JMSException;

import com.welflex.jms.annotations.DestinationJndi;

public class ProxyFactory {

  public static <T> T create(Class<T> clazz, MessageSendExecutor executor) throws JMSException {
    boolean hasSendMethod = false;
    for (Method method : clazz.getMethods()) {
      hasSendMethod = method.isAnnotationPresent(DestinationJndi.class);
      if (hasSendMethod) {
        break;
      }
    }
    
    if (!hasSendMethod) {
      throw new RuntimeException("One needs to have atleast one send method annotated with a Destination");
    }

    Class<?> intf[] = { clazz };
    MessageSendProxy sendProxy = new MessageSendProxy(executor, clazz);

    @SuppressWarnings("unchecked")
    T proxy = (T) Proxy.newProxyInstance(clazz.getClassLoader(), intf, sendProxy);

    return proxy;
  }
}
