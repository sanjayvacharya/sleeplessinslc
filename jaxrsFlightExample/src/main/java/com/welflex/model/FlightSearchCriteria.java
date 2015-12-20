package com.welflex.model;
import javax.ws.rs.FormParam;
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
  @FormParam("fromAirportCode")
  private String fromAirportCode;

  @XmlElement
  @FormParam("toAirportCode")
  private String toAirportCode;

  public FlightSearchCriteria() {}
  
  public FlightSearchCriteria(String fromAirportCode, String toAirportCode) {
    this.fromAirportCode = fromAirportCode;
    this.toAirportCode = toAirportCode;
  }

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
