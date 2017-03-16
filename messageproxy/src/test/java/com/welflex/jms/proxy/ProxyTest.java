package com.welflex.jms.proxy;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;

import com.welflex.jms.annotations.DestinationJndi;
import com.welflex.jms.annotations.MessageType;

public class ProxyTest {
  
  public static interface Sender {
    @DestinationJndi("bar")
    @MessageType(ObjectMessage.class)
    public void sendObjectMessage(String s);

    @DestinationJndi("baz")
    @MessageType(TextMessage.class)
    public void sendStringMessage(String s);

    @DestinationJndi("taz")
    @MessageType(MapMessage.class)
    public void sendMapMessage(Map<String, Object> map);
  }
  
  private Message receive(Session session, String destination) throws JMSException {
    MessageConsumer consumer = session.createConsumer(executor.lookup(destination));
    return consumer.receive();    
  }
  
  private ActiveMQSendExecutor executor;
  
  @Test
  public void testProxy() throws JMSException {
    executor = new ActiveMQSendExecutor("vm://localhost", "foo", "bar");
    Sender s = ProxyFactory.create(Sender.class, executor);
    s.equals(s);
    s.sendObjectMessage("Foo");
    s.sendStringMessage("Bar");
    
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("name", "Sanjay");
    m.put("age", 23);
    m.put("salary", 12313131.213F);
    
    s.sendMapMessage(m);
    
    ConnectionFactory cf = executor.getConnectionFactory();
    Connection conn = cf.createConnection();
    conn.start();
    
    Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
    Message msg = receive(sess, "bar");
    assertTrue(msg instanceof ObjectMessage);
    ObjectMessage om = (ObjectMessage) msg;
    System.out.println("Object returned:" + om.getObject());
    
    msg = receive(sess, "baz");
    assertTrue(msg instanceof TextMessage);
    TextMessage tm = (TextMessage) msg;
    System.out.println("String returned:" + tm.getText());
    
    msg = receive(sess, "taz");
    assertTrue(msg instanceof MapMessage);
    MapMessage mapMsg = (MapMessage) msg;
    System.out.println("Map returned:name=" + mapMsg.getString("name") 
      + ", Age:" + mapMsg.getInt("age") + ",Salary:" + mapMsg.getFloat("salary"));
  }
}
  