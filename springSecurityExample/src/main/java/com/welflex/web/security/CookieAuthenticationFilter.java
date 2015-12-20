package com.welflex.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.web.filter.GenericFilterBean;

public class CookieAuthenticationFilter extends GenericFilterBean {

  private final LdapUserDetailsService userDetailService;

  private final CookieService cookieService;

  public CookieAuthenticationFilter(LdapUserDetailsService userDetailService,
      CookieService cookieService) {
    this.userDetailService = userDetailService;
    this.cookieService = cookieService;
  }

  static final String FILTER_APPLIED = "__spring_security_scpf_applied";

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
    ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (request.getAttribute(FILTER_APPLIED) != null) {
      // ensure that filter is only applied once per request
      chain.doFilter(request, response);
      return;
    }

    request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

    SecurityContext contextBeforeChainExecution = loadSecurityContext(request);

    try {
      SecurityContextHolder.setContext(contextBeforeChainExecution);
      chain.doFilter(request, response);
    }
    finally {
     // Clear the context and free the thread local
      SecurityContextHolder.clearContext();
      request.removeAttribute(FILTER_APPLIED);
    }
  }

  /**
   * Loads information such as roles etc about the user if the user cookie were present.
   * This is a great place to introduce a cache if required to prevent hitting ldap on every call.
   * 
   * @param request HttpServletRequest
   * @return A SecurityContext
   */
  private SecurityContext loadSecurityContext(HttpServletRequest request) {
    final String userName = cookieService.extractUserName(request.getCookies());

    return userName != null
        ? new CookieSecurityContext(userDetailService.loadUserByUsername(userName))
        : SecurityContextHolder.createEmptyContext();
  }
}
