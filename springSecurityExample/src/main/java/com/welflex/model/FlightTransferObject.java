package com.welflex.model;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.welflex.validation.HourMinute;

public class FlightTransferObject {
  private Long id;

  @NotBlank(message = "must not be blank")
  private String number;

  @NotBlank(message = "must not be blank")
  private String fromAirportCode;

  @NotBlank(message = "must not be blank")
  private String toAirportCode;

  @NotNull
  private Date departureDate;
  
  @HourMinute
  private String departureTime;

  @NotNull
  private Date arrivalDate;
 
  @HourMinute
  private String arrivalTime;
  
  @Min(value = 1)
  private int numberOfSeats = 10;

  @Min(value = 100)
  private int miles;
  
  public int getMiles() {
    return miles;
  }

  public void setMiles(int miles) {
    this.miles = miles;
  }

  public int getNumberOfSeats() {
    return numberOfSeats;
  }

  public void setNumberOfSeats(int numberOfSeats) {
    this.numberOfSeats = numberOfSeats;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
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

  public Date getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(Date departureDate) {
    this.departureDate = departureDate;
  }

  public String getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(String departureTime) {
    this.departureTime = departureTime;
  }

  public Date getArrivalDate() {
    return arrivalDate;
  }

  public void setArrivalDate(Date arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  public String getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(String arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

}
