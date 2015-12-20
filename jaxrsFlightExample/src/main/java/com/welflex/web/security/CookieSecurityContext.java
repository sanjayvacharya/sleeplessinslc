package com.welflex.web.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

public class CookieSecurityContext implements SecurityContext {
  private static final long serialVersionUID = 1L;
  
  private Authentication authentication;
  
  public CookieSecurityContext(UserDetails userDetails) {
    this.authentication = new AuthenticationImpl(userDetails);
  }
  
  @Override
  public Authentication getAuthentication() {
    return authentication;
  }

  @Override
  public void setAuthentication(Authentication authentication) {
    throw new UnsupportedOperationException("Should not be called by the code path");
  }
  
  private static class AuthenticationImpl implements Authentication {
    private static final long serialVersionUID = 1L;
    private final UserDetails userDetails;
    
    public AuthenticationImpl(UserDetails userDetails) {
      this.userDetails = userDetails;
    }
    
    @Override
    public String getName() {
      return userDetails.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
      return userDetails.getPassword();
    }

    @Override
    public Object getDetails() {
      return userDetails;
    }

    @Override
    public Object getPrincipal() {
      return userDetails.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
      return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
      throw new UnsupportedOperationException("Should not be invoked from any code downstream");
    }
  }

}
