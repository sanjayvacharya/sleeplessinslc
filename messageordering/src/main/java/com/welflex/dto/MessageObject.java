package com.welflex.dto;

import java.io.Serializable;

/**
 * A Message Sent
 */
public class MessageObject implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long recordId;
  private String firstName;
  private String lastName;

    
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

  public MessageObject() {}

  public MessageObject(Long recordId) {
    this.recordId = recordId;
  }

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }
}
