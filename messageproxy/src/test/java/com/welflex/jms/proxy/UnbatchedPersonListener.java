package com.welflex.jms.proxy;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.hibernate.Transaction;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.welflex.dto.PersonDto;
import com.welflex.hibernate.HibernateUtil;
import com.welflex.hibernate.model.Person;

public class UnbatchedPersonListener implements SessionAwareMessageListener<ObjectMessage> {
  CountDownLatch latch = null;

  public void onMessage(ObjectMessage om, Session session) throws JMSException {
    org.hibernate.Session hsession = HibernateUtil.currentSession();
    Transaction tx = null;
    try {
      tx = hsession.beginTransaction();
      hsession.save(createPerson((PersonDto) om.getObject()));
      tx.commit();
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
}
