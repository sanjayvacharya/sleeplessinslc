package com.welflex.jms.proxy;

import java.util.concurrent.CountDownLatch;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.junit.Test;

import com.welflex.dto.PersonDto;
import com.welflex.jms.annotations.DestinationJndi;
import com.welflex.jms.annotations.MessageType;
import com.welflex.jms.spring.BatchMessageListenerContainer;

public class BatchListenerTest {

  // Sender
  private static interface Sender {
    @DestinationJndi("testqueue")
    @MessageType(ObjectMessage.class)
    public void send(PersonDto dto);
  }
  
  private static final int MESSAGE_COUNT = 1323;

  @Test(timeout=30000)
  public void demonstrateBatchMessageListener() throws JMSException, InterruptedException {
    ActiveMQSendExecutor executor = new ActiveMQSendExecutor("vm://localhost", "foo", "bar");
    ConnectionFactory connFactory = executor.getConnectionFactory();
    Destination dest = executor.lookup("testqueue");
    Sender s = ProxyFactory.create(Sender.class, executor);
    
    for (int i = 0; i < MESSAGE_COUNT; i++) {
      s.send(new PersonDto("Donald:" + i, "Duck:" + i));
    }
    
    CountDownLatch latch = new CountDownLatch(MESSAGE_COUNT);
    BatchPersonListener b = new BatchPersonListener();
    b.latch = latch;
    
    BatchMessageListenerContainer container = new BatchMessageListenerContainer();
    
    // Set a batch size of 200 messages
    container.setBatchSize(200);
    container.setCacheLevel(BatchMessageListenerContainer.CACHE_CONSUMER);
    container.setConnectionFactory(connFactory);
    container.setDestination(dest);
    container.setMessageListener(b);
    container.setConcurrentConsumers(5);
    container.setMaxConcurrentConsumers(10);
    container.initialize();

   
   
    container.start();
    latch.await();           
  }
}
