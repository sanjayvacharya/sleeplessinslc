package com.welflex.jms.proxy;

import javax.jms.Message;

public class MessageSendRequest {
  private String destination;

  private Class<? extends Message> messageType;

  private Object payload;

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  @Override
  public String toString() {
    return "MessageSendRequest [destination=" + destination + ", messageType=" + messageType
      + ", payload=" + payload + "]";
  }

  public Class<? extends Message> getMessageType() {
    return messageType;
  }

  public void setMessageType(Class<? extends Message> messageType) {
    this.messageType = messageType;
  }

  public Object getPayload() {
    return payload;
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }
}
