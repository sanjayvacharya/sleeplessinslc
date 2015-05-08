package com.welflex.notes.jaxb;

import javax.xml.bind.annotation.XmlRootElement;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@XmlRootElement
@AutoProperty
public class Note {
  private String userId;

  private String content;

  public Note() {}
  
  public Note(String userId, String content) {
    this.userId = userId;
    this.content = content;
  }
  
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
  
  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
  
  @Override
  public boolean equals(Object other) {
    return Pojomatic.equals(this, other);
  }
  
  @Override
  public int hashCode() {
    return Pojomatic.hashCode(this);
  }
}
