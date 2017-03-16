package com.welflex.jms.proxy;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.welflex.dto.PersonDto;
import com.welflex.hibernate.HibernateUtil;
import com.welflex.hibernate.model.Person;

import com.welflex.jms.spring.SessionAwareBatchMessageListener;

public class BatchPersonListener implements SessionAwareBatchMessageListener<ObjectMessage> {
  private static final Logger LOG = Logger.getLogger(BatchPersonListener.class);
  private static final int HIB_BATCH_SIZE = 100;
 
  CountDownLatch latch = null;
  
  public void onMessages(Session session, List<ObjectMessage> messages) throws JMSException {
    org.hibernate.Session hsession = HibernateUtil.currentSession();
    Transaction tx = null;
    try {
      tx = hsession.beginTransaction();
      int counter = 0;
      
      for (ObjectMessage om : messages) {
        hsession.save(createPerson((PersonDto) om.getObject()));
        if (counter % HIB_BATCH_SIZE == 0) {    
          hsession.flush();
          hsession.clear();
        }
        counter++;
      }
      tx.commit();  
      countdown(messages.size());
      if (LOG.isDebugEnabled()) {
        LOG.debug("Commited:" + messages.size() + " Person records to the datastore");
      }
    }
    catch (Exception e) {
      if (tx != null) {
        tx.rollback();
      }
      throw new RuntimeException("Error creating person", e);
    }
    finally {
      HibernateUtil.closeSession();
    }
  }
  
  private Person createPerson(PersonDto om) {
    Person p = new Person();
    p.setFirstName(om.getFirstName());
    p.setLastName(om.getLastName());
    return p;
  }
  
  private void countdown(int size) {
    if (latch == null) {
      return;      
    }
    for (int i = 0; i < size; i++) {
      latch.countDown();
    }
    LOG.debug("Current countdown:" + latch.getCount());
  }
}
