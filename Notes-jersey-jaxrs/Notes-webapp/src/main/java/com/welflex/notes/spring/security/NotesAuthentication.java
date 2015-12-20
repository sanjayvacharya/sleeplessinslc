package com.welflex.notes.spring.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class NotesAuthentication extends AbstractAuthenticationToken {
  private static final long serialVersionUID = 1L;

  public NotesAuthentication(Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    setAuthenticated(true);
  }
  
  public boolean isAuthenticated() {
    return true;
  }

  @Override public Object getCredentials() {
    return "";
  }

  @Override public Object getPrincipal() {
    return "";
  } 
}
