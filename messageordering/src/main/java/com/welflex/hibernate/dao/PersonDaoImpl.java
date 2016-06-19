package com.welflex.hibernate.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.welflex.exception.StaleObjectException;
import com.welflex.hibernate.model.Person;

public class PersonDaoImpl implements PersonDao {
  private static final Logger LOG = Logger.getLogger(PersonDaoImpl.class);

  @Override
  public Person read(Long recordId, Session session) {
    return (Person) session.load(Person.class, recordId);
  }
  
  @Override
  public void saveOrUpdate(Person person, Session session) {
    // Read needs to be done to bring object into session with the correct version
    // This will get ready anyway, so might as well read it
    Person model = (Person) session.get(Person.class, person.getRecordId());
    
    if (model == null) {
      // If not presnt create the downstream object
      LOG.info("Person is not present in Db with record id:" + person.getRecordId());
      model = new Person();
    } else if (model.getJmsTimestamp().compareTo(person.getJmsTimestamp()) > 0){
      throw new StaleObjectException("Database time for the record is [" + model.getJmsTimestamp().getTime() 
        + "]"
        + ", attempted to update record using older time stamp [" + person.getJmsTimestamp().getTime() + "]");
    }
    
    model.setFirstName(person.getFirstName());
    model.setLastName(person.getLastName());
    model.setRecordId(person.getRecordId());
    model.setJmsTimestamp(person.getJmsTimestamp());

    session.merge(model);
    session.flush();
    // Optimistic locking with the @Version of person will handle any updates that have occured
    // while this process was being completed.
  }
}
