package com.welflex.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FlightSearchCriteria {
  @XmlElement
  private String fromAirportCode;

  @XmlElement
  private String toAirportCode;

  public FlightSearchCriteria() {}

  public String getFromAirportCode() {
    return fromAirportCode;
  }

  public void setFromAirportCode(String fromAirportCode) {
    this.fromAirportCode = fromAirportCode;
  }

  public String getToAirportCode() {
    return toAirportCode;
  }

  public void setToAirportCode(String toAirportCode) {
    this.toAirportCode = toAirportCode;
  }
  
  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
}
