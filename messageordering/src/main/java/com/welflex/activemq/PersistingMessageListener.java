package com.welflex.activemq;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import com.welflex.dto.MessageObject;
import com.welflex.hibernate.dao.PersonDaoImpl;
import com.welflex.hibernate.model.Person;
import com.welflex.service.PersonService;
import com.welflex.service.PersonServiceImpl;

/**
 * A Listener that upadates the underlying datastore with the record
 */
public class PersistingMessageListener extends AbstractListener {
  private PersonService personService;
  
  public PersistingMessageListener(CountDownLatch latch) {
    super(latch);
    this.personService = new PersonServiceImpl(new PersonDaoImpl());
  }

  @Override
  protected void doWithMessage(MessageObject m, long timeStamp) {
      Person p = new Person();
      p.setFirstName(m.getFirstName());
      p.setLastName(m.getLastName());
      p.setRecordId(m.getRecordId());
      p.setJmsTimestamp(new Date(timeStamp));
      personService.savePerson(p);      
  }
}
