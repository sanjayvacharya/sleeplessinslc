package com.welflex.jms.proxy;

import javax.jms.JMSException;

/**
 * Defines an executor.
 * 
 * @author Sanjay Acharya
 */
public interface MessageSendExecutor {
  /**
   * Sends a message Request
   * 
   * @param request Request object
   * @throws JMSException If an error occurs
   */
  public void send(MessageSendRequest request) throws JMSException;
}
