package com.welflex.jms.proxy;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.map.LazyMap;
import org.apache.log4j.Logger;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;

public class ActiveMQSendExecutor implements MessageSendExecutor {
  private static final Logger LOG = Logger.getLogger(ActiveMQSendExecutor.class);

  private final ConnectionFactory cf;

  private JmsTemplate template;

  private Map<String, Destination> map;

  
  @SuppressWarnings("unchecked")
  public ActiveMQSendExecutor(String url, String userName, String password) {
    this.cf = new ActiveMQConnectionFactory(userName, password, url);
    CachingConnectionFactory ccf = new CachingConnectionFactory(cf);
    ccf.setCacheProducers(true);
    ccf.setSessionCacheSize(100);

    template = new JmsTemplate(ccf);   
    map = LazyMap.decorate(Collections.synchronizedMap(new HashMap<String, Destination>()), new Transformer() {

      public Object transform(Object key) {
        Session s;
        try {
          s = cf.createConnection().createSession(false, Session.AUTO_ACKNOWLEDGE);
          return s.createQueue(String.valueOf(key));

        }
        catch (JMSException e) {
          throw new RuntimeException(e);
        }
      }
      
    });
  }

  @SuppressWarnings("unchecked")
  public void send(final MessageSendRequest request) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Sending Message:" + request);
    }
    template.execute(new ProducerCallback() {
      public Object doInJms(Session session, MessageProducer producer) throws JMSException {
        Message message = createMessage(session, request.getMessageType(), request.getPayload());
        producer.send(lookup(request.getDestination()), message);
        return null;
      }
    });
  }

  private Message createMessage(Session session, Class<? extends Message> messageType,
    Object payLoad) throws JMSException {

    if (messageType.equals(ObjectMessage.class)) {
      ObjectMessage message = session.createObjectMessage();
      message.setObject((Serializable) payLoad);
      return message;
    }
    else if (messageType.equals(TextMessage.class)) {
      TextMessage tm = session.createTextMessage(payLoad.toString());
      return tm;
    }
    else if (messageType.equals(MapMessage.class)) {
      if (!(payLoad instanceof Map<?, ?>)) {
        throw new RuntimeException("If using a Map message, object must be a map");
      }

      @SuppressWarnings("unchecked")
      Map<String, ?> map = (Map<String, ?>) payLoad;
      MapMessage mm = session.createMapMessage();

      for (Map.Entry<String, ?> elem : map.entrySet()) {
        if (elem.getValue().getClass().equals(Integer.class)) {
          mm.setInt(elem.getKey(), ((Integer) elem.getValue()).intValue());
        }
        else if (elem.getValue().getClass().equals(String.class)) {
          mm.setString(elem.getKey(), elem.getValue().toString());
        }
        else if (elem.getValue().getClass().equals(Double.class)) {
          mm.setDouble(elem.getKey(), ((Double) elem.getValue()).doubleValue());
        }
        else if (elem.getValue().getClass().equals(Float.class)) {
          mm.setFloat(elem.getKey(), ((Float) elem.getValue()).floatValue());
        }
        else {
          mm.setObject(elem.getKey(), elem.getValue());
        }
      }
      return mm;
    }
    else {
      throw new IllegalStateException("Not supported message type");
    }
  }

  public Destination lookup(String destination) {
    return map.get(destination);
  }
  
  public ConnectionFactory getConnectionFactory() {
    return cf;
  }
}
