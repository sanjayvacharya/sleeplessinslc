package com.welflex.service;

import com.welflex.exception.StaleObjectException;
import com.welflex.hibernate.model.Person;

public interface PersonService {
  public void savePerson(Person person) throws StaleObjectException;
  
  public Person readPerson(Long recordId);
}
