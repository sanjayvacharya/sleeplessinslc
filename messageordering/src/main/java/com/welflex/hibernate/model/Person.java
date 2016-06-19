package com.welflex.hibernate.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.AccessType;

@Entity
@Table(name = "PERSON")
public class Person implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @AccessType(value = "property")
  private Long recordId;

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;
  
  // JMS Record time stamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "JMS_TIMESTAMP", nullable = false)
  private Date jmsTimestamp;

  // Versioning for Hibernate
  @Version
  @Column(name = "OPT_VER", nullable = false)
  @AccessType(value = "property")
  private Long version;

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Date getJmsTimestamp() {
    return jmsTimestamp;
  }

  public void setJmsTimestamp(Date jmsTimestamp) {
    this.jmsTimestamp = jmsTimestamp;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Person() {}

  public Person(Long recordId) {
    this.recordId = recordId;
  }

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }
}
