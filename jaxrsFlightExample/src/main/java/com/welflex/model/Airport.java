package com.welflex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@XmlRootElement
@AutoProperty
@Entity
@Table(name = "AIRPORT")
public class Airport {

  @Id
  @Column(name = "CODE")
  @XmlElement
  @NotBlank(message = "must not be blank")
  private String code;

  @Column(name = "NAME")
  @XmlElement
  @NotBlank(message = "must not be blank")
  private String name;

  @Column(name = "CITY")
  @XmlElement
  @NotBlank(message = "must not be blank")
  private String city;

  public Airport() {}

  public Airport(String code, String name, String city) {
    this.code = code;
    this.name = name;
    this.city = city;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCity() {
    return city;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public boolean equals(Object other) {
    return Pojomatic.equals(this, other);
  }

  public int hashCode() {
    return Pojomatic.hashCode(this);
  }

  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
}
