package com.welflex.web.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CookieLogoutHandler implements LogoutHandler {
  private final CookieService cookieService;
  
  public CookieLogoutHandler(CookieService cookieService) {
    this.cookieService = cookieService;
  }
  
  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) {
   
    Cookie cookie = cookieService.getCookie(CookieService.COOKIE_NAME, request.getCookies());
    
    if (cookie != null) {
      Cookie empty = new Cookie(CookieService.COOKIE_NAME, "");
      empty.setMaxAge(0);
      response.addCookie(empty);
    }
    SecurityContextHolder.clearContext();
  }
}
