package com.welflex.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table (name="NATIONAL_PLAYER")
public class NationalPlayer {
  @Id @GeneratedValue(generator="PlayerIdGenerator")
  @GenericGenerator(name="PlayerIdGenerator", strategy="org.hibernate.shards.id.ShardedUUIDGenerator")
  @Column(name="PLAYER_ID")
  private BigInteger id;

  @Column (name="FIRST_NAME")
  private String firstName;

  @Column (name="LAST_NAME")
  private String lastName;
  
  @Column (name="CAREER_GOALS")
  private int careerGoals;
  
  @Column (name="WORLD_RANKING")
  private int worldRanking;

  public BigInteger getId() {
    return id;
  }
  
  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Country getCountry() {
    return country;
  }

  public int getCareerGoals() {
    return careerGoals;
  }

  public int getWorldRanking() {
    return worldRanking;
  }
  
  public NationalPlayer withCountry(Country country) {
    this.country = country;
    return this;
  }
  
  public NationalPlayer withCareerGoals(int careerGoals) {
    this.careerGoals = careerGoals;
    
    return this;
  }
  
  public NationalPlayer withWorldRanking(int worldRanking) {
    this.worldRanking = worldRanking;
    
    return this;
  }
  
  public NationalPlayer withFirstName(String firstName) {
    this.firstName = firstName;
    
    return this;
  }
  
  public NationalPlayer withLastName(String lastName) {
    this.lastName = lastName;
    
    return this;
  }
  
  @Column(name= "COUNTRY", columnDefinition="integer", nullable = true)
  @Type(
      type = "com.welflex.hibernate.GenericEnumUserType",
      parameters = {
              @Parameter(
                  name  = "enumClass",                      
                  value = "com.welflex.model.Country"),
              @Parameter(
                  name  = "identifierMethod",
                  value = "toInt"),
              @Parameter(
                  name  = "valueOfMethod",
                  value = "fromInt")
              }
  )
  
  private Country country;
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
    
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((firstName == null)
        ? 0
        : firstName.hashCode());
    result = prime * result + ((lastName == null)
        ? 0
        : lastName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NationalPlayer other = (NationalPlayer) obj;
    if (firstName == null) {
      if (other.firstName != null)
        return false;
    }
    else if (!firstName.equals(other.firstName))
      return false;
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    }
    else if (!lastName.equals(other.lastName))
      return false;
    return true;
  }
}
