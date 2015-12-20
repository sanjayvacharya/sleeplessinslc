package com.welflex.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@XmlRootElement
@AutoProperty
public class User {
  @NotBlank(message = "User must not be blank")
  private String userName;

  @NotBlank(message = "Password must not be blank")
  private String password;
  
  public static final String USER_NAME_PROPERTY = "userName";
  
  public static final String PASSWORD_PROPERTY = "password";
  
  public User() {}

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  
  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }
}
