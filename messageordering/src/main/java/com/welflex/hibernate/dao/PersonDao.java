package com.welflex.hibernate.dao;

import org.hibernate.Session;

import com.welflex.hibernate.model.Person;

/**
 * Person Hibernate DAO.
 */
public interface PersonDao {
  void saveOrUpdate(Person person, Session session);
  Person read(Long recordId, Session session);
}
