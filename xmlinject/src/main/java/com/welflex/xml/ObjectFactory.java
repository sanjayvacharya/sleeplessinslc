package com.welflex.xml;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
  public ObjectFactory() {}

  public Person createPerson() {
    return new Person();
  }

  public Item createItem() {
    return new Item();
  }
}
