package com.welflex.activemq;

import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

import com.welflex.dto.MessageObject;
import com.welflex.hibernate.dao.PersonDaoImpl;
import com.welflex.hibernate.model.Person;
import com.welflex.service.CacheService;
import com.welflex.service.PersonService;
import com.welflex.service.PersonServiceImpl;

/**
 * Listener that uses the Coherence Cache Service and updates an underlying
 * database.
 */
public class CacheMessageListener extends AbstractListener {
  private static final Logger LOG = Logger.getLogger(CacheMessageListener.class);
  private final CacheService cacheService;
  private final PersonService personService;
  
  public CacheMessageListener(CacheService cacheService, CountDownLatch latch) {
    super(latch);
    this.cacheService = cacheService;
    this.personService = new PersonServiceImpl(new PersonDaoImpl());
  }

  @Override
  protected void doWithMessage(final MessageObject mo, final long timeStamp) {
    cacheService.doCacheAction(mo.getRecordId().toString(), timeStamp,
      new CacheService.CacheAction<Void, RuntimeException>() {
        public Void doAction() throws RuntimeException {
          Person p = new Person();
          p.setRecordId(mo.getRecordId());
          p.setFirstName(mo.getFirstName());
          p.setLastName(mo.getLastName());
          p.setJmsTimestamp(new Timestamp(timeStamp));
          personService.savePerson(p);
          LOG.info("Persisted Record:" + mo.getRecordId());
          return null;
        }
      }); 
  }
}
