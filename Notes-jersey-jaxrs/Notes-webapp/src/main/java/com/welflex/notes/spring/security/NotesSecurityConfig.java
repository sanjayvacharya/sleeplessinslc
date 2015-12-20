package com.welflex.notes.spring.security;

import javax.servlet.Filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.google.common.collect.ImmutableList;

@Configuration
@ImportResource("classpath:/security-context.xml")
public class NotesSecurityConfig {
  
  @Bean
  public FilterChainProxy springSecurityFilterChain() {
    SecurityFilterChain chain = new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"), 
      ImmutableList.<Filter>builder()  
      .add(new SecurityContextPersistenceFilter(new NotesSecurityContextRepository()))
      .build());
    
    return new FilterChainProxy(ImmutableList.of(chain));
  }
}
