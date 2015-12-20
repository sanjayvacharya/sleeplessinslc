package com.welflex.web.security;

import javax.servlet.http.Cookie;

/**
 * This is a naive Cookie implementation. In a true enviorment, one would need 
 * to create a more secure cookie that cannot be spoofed. Use SHA, Salts what have you.
 * For the scope of this example, it will have to do though.
 */
public interface CookieService {
  static final String COOKIE_NAME = "AUTHCOOKIE";
  
  public String extractUserName(Cookie[] cookies);
  public Cookie getCookie(String cookieName, Cookie[] cookies);
  public Cookie createCookie(String userName);
  
  public static class Impl implements CookieService {
    
    @Override
    public String extractUserName(Cookie[] cookies) {
      Cookie cookie = getCookie(COOKIE_NAME, cookies);
      return cookie == null ? null : cookie.getValue();
    }

    @Override
    public Cookie createCookie(String userName) {
      Cookie c = new Cookie(COOKIE_NAME, userName);
      c.setPath("/");                 
      
      return c;
    }

    @Override
    public Cookie getCookie(String cookieName, Cookie[] cookies) {
      if (cookies == null) {
        return null;
      }
      
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(cookieName)) {
          return cookie;
        }
      }

      return null;
    }
  }
}
