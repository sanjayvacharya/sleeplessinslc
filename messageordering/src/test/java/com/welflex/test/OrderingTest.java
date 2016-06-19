package com.welflex.test;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.welflex.activemq.CacheMessageListener;
import com.welflex.activemq.PersistingMessageListener;
import com.welflex.activemq.Util;
import com.welflex.dto.MessageObject;
import com.welflex.hibernate.dao.PersonDaoImpl;
import com.welflex.hibernate.model.Person;
import com.welflex.service.CacheService;
import com.welflex.service.CoherenceCacheServiceImpl;
import com.welflex.service.PersonService;
import com.welflex.service.PersonServiceImpl;

/**
 * Demonstrates use of Optimistic and Perssimistic locking using Hibernate 
 * and Coherence respectively. Its not a validating test but a demo.
 */
public class OrderingTest {
  
  @Before
  public void before() {
    primeDb();
  }

  private DefaultMessageListenerContainer startContainer(ConnectionFactory connFactory,
    MessageListener listener, Destination destination) {
    DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();

    container.setConnectionFactory(connFactory);
    container.setConcurrentConsumers(3);
    container.setMaxConcurrentConsumers(4);
    container.setDestination(destination);
    container.setMessageListener(listener);
    container.afterPropertiesSet();
    container.start();

    return container;
  }

  private static final int MAX_MSG = 100;

  @Test
  public void testCacheLocking() throws Exception {
    CacheService cacheService = new CoherenceCacheServiceImpl();
    DefaultMessageListenerContainer container = null;
    try {
      ConnectionFactory connFactory = Util.createConnectionFactory("foo");
      Connection connection = connFactory.createConnection();
      connection.start();
      Session prouducerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Destination dest = prouducerSession.createQueue("fooQ");
     
      CountDownLatch latch = new CountDownLatch(MAX_MSG);
      CacheMessageListener lsnr = new CacheMessageListener(cacheService, latch);
      container = startContainer(connFactory, lsnr, dest);
      produceMessages(prouducerSession, dest);
      latch.await();
    }
    finally {
      if (container != null) {
        container.shutdown();
      }
      cacheService.shutdown();
    }
  }
  
  /**
   * Insert records expected for updates
   */
  private void primeDb() {
    System.out.println("Priming database...");
    PersonService service = new PersonServiceImpl(new PersonDaoImpl());
    for (int i = 0; i < Util.RECORD_IDS.length; i++) {
      Person p = new Person(Util.RECORD_IDS[i]);
      p.setFirstName("Foo");
      p.setLastName("Bar");
      p.setJmsTimestamp(new Timestamp(System.currentTimeMillis()));
      service.savePerson(p);
    }
  }

  @Test
  public void testDbOptimisticLocking() throws Exception {
    ConnectionFactory connFactory = Util.createConnectionFactory("foo");
    Connection connection = connFactory.createConnection();
    connection.start();
    Session prouducerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    Destination dest = prouducerSession.createQueue("fooQ");
  
    CountDownLatch latch = new CountDownLatch(MAX_MSG);
    PersistingMessageListener lsnr = new PersistingMessageListener(latch);
    DefaultMessageListenerContainer container = startContainer(connFactory, lsnr, dest);
    produceMessages(prouducerSession, dest);
    latch.await();
    container.shutdown();
  }

  private void produceMessages(Session session, Destination dest) throws Exception {
    MessageProducer producer = session.createProducer(dest);
    Random random = new Random();

    for (int i = 0; i < MAX_MSG; i++) {
      MessageObject record = new MessageObject(Util.createRandomRecordId());
      record.setFirstName("Foo " + i);
      record.setLastName("Bar " + i);

      ObjectMessage o = session.createObjectMessage();
      o.setObject(record);
      producer.send(o);
      // Sleep a bit
      Thread.sleep(random.nextInt(10));
    }
  }
}
