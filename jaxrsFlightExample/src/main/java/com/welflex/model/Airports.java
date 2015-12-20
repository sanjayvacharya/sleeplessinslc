package com.welflex.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@XmlRootElement
@AutoProperty
public class Airports {
  
  private List<Airport> airport;
  
  public Airports() {}
  
  public Airports(List<Airport> airport) {
    this.airport = airport;
  }
  
  public List<Airport> getAirport() {
    return airport;
  }

  public void setAirport(List<Airport> airport) {
    this.airport = airport;
  }
  
  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
}
