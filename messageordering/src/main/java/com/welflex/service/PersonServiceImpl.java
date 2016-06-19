package com.welflex.service;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;

import com.welflex.exception.StaleObjectException;
import com.welflex.hibernate.HibernateUtil;
import com.welflex.hibernate.dao.PersonDao;
import com.welflex.hibernate.model.Person;

/**
 * Note the DAO does not use standard Hibernate pattern but opens and 
 * closes sessions on every action. Reason is hibernate sessions should not 
 * be re-used after any exception occurs.
 */
public class PersonServiceImpl implements PersonService {
  private PersonDao personDao;
  private static final Logger LOG = Logger.getLogger(PersonServiceImpl.class);
  
  public PersonServiceImpl(PersonDao personDao) {
    this.personDao = personDao;
  }
  
  /**
   * Logic is as follows. 
   * Attempt to save/update person. 
   * If a StaleObjectException is received, its because some other record
   * got through first, not necessarily because its JMS time stamp was later.
   * Try again - its possible that failure could occur with another message
   * conflicting with concurrent update, willing to take that chance here on a
   * very minute window.
   */
  @Override
  public void savePerson(Person person) throws StaleObjectException {
    for (int i = 0; i < 2; i++) {
      Session session = HibernateUtil.currentSession();
      Transaction tx = null;

      try {
        tx = session.beginTransaction();
        personDao.saveOrUpdate(person, session);
        tx.commit();
        return;
      }
      catch (Exception e) {
        if (tx != null) {
          tx.rollback();
        }
        
        if (e instanceof StaleObjectException) {
          throw ((StaleObjectException) e);
        } else if (!(e instanceof StaleObjectStateException)) {
          throw new RuntimeException("Error processing record:", e);
        } else {
          LOG.info("Detected a Stale Record retrying....");
        }
      }
      finally {
        HibernateUtil.closeSession();
      }
    }
  }

  @Override
  public Person readPerson(Long recordId) {
    Session session = HibernateUtil.currentSession();
    Transaction tx = null;

    try {
      tx = session.beginTransaction();
      Person p = personDao.read(recordId, session);
      p.getJmsTimestamp();
      tx.commit();
      return p;
    }
    catch (RuntimeException e) {
      if (tx != null) {
        tx.rollback();
      }
      throw e;
    }
    finally {
      HibernateUtil.closeSession();
    }
  }
}
