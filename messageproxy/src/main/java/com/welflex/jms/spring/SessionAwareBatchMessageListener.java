package com.welflex.jms.spring;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public interface SessionAwareBatchMessageListener<M extends Message> {
  /**
   * Perform a batch action with the provided list of {@code messages}.
   * 
   * @param session JMS {@code Session} that received the messages
   * @param messages List of messages
   * @throws JMSException JMSException thrown if there is an error performing the operation.
   */
  public void onMessages(Session session, List<M> messages) throws JMSException;
}
