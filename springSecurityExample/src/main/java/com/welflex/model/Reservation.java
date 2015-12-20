package com.welflex.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@AutoProperty
public class Reservation {
  @XmlElement
  @NotBlank(message = "must not be blank")
  private String reservationName;

  @Min(1)
  @XmlElement
  private int quantity = 1;

  @XmlElement
  @NotNull(message = "Flight Id must be provided")
  private Long flightId;

  public Long getFlightId() {
    return flightId;
  }

  public void setFlightId(Long flightId) {
    this.flightId = flightId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public String getReservationName() {
    return reservationName;
  }

  public void setReservationName(String reservationName) {
    this.reservationName = reservationName;
  }
  
  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
}
