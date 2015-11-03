package com.welflex.spring.web.notes;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
  public Note createNote() {
    return new Note();
  }
}
