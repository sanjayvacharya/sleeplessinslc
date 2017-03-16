package com.welflex.jms.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.jms.Message;

@Retention(RetentionPolicy.RUNTIME)
public @interface MessageType {
  public Class<? extends Message> value();
}
