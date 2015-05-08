package com.welflex.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
  
  @Bean
  public NotesService notesService() {
    return new NotesServiceImpl();
  }
  
  @Bean
  public NotesHealthService health() {
    return new NotesHealthServiceImpl();
  }
}
