package com.welflex.notes.spring.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import com.google.common.collect.Sets;

public class NotesSecurityContextRepository implements SecurityContextRepository {

  @Override 
  public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
    HttpServletRequest request = requestResponseHolder.getRequest();
    String roleHeader = request.getHeader("role");
    
    Authentication auth = null;
    
    if (roleHeader != null) {
      auth = new NotesAuthentication(Sets.<GrantedAuthority>newHashSet(new SimpleGrantedAuthority(roleHeader)));      
    }
    else {
      auth = new NotesAuthentication(Sets.<GrantedAuthority>newHashSet());
    }
    
    SecurityContext ctx = new SecurityContextImpl();
    ctx.setAuthentication(auth);
    
    return ctx;
  }

  @Override 
  public void saveContext(SecurityContext context, HttpServletRequest request,
    HttpServletResponse response) {   
  }

  @Override public boolean containsContext(HttpServletRequest request) {
    return true;
  }

}
