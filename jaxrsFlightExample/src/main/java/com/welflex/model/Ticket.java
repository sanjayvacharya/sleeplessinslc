package com.welflex.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "TICKET")
public class Ticket implements Serializable {
  private static final long serialVersionUID = 9055289225897889158L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @XmlElement(name="ticketId")
  private Long id;

  @Column(name = "ISSUE_DATE")
  @Type(type = "com.welflex.model.hibernate.LocalDateUserType")
  @XmlElement
  private LocalDate issueDate;

  @ManyToOne
  @JoinColumn(name = "FLIGHT_ID", nullable = false)
  private Flight flight;

  @XmlElement
  private String reservationName;

  @XmlElement
  private int numberOfSeats;

  public Ticket() {}

  public Ticket(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public int getNumberOfSeats() {
    return numberOfSeats;
  }

  public void setNumberOfSeats(int numberOfSeats) {
    this.numberOfSeats = numberOfSeats;
  }

  public Flight getFlight() {
    return flight;
  }

  public void setFlight(Flight flight) {
    this.flight = flight;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(LocalDate issueDate) {
    this.issueDate = issueDate;
  }

  public String getReservationName() {
    return reservationName;
  }

  public void setReservationName(String reservationName) {
    this.reservationName = reservationName;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
}
