package com.welflex.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Reservations {
  private List<Ticket> tickets;

  public Reservations() {}

  public Reservations(List<Ticket> tickets) {
    this.tickets = tickets;
  }

  public List<Ticket> getTickets() {
    return tickets;
  }

  public void setTickets(List<Ticket> tickets) {
    this.tickets = tickets;
  }
  
  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
}
