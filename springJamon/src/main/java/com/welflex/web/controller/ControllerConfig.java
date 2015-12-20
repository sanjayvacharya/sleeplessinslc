package com.welflex.web.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {
  @Bean
  public LoginController getLoginController() {
    return new LoginController();
  }
}
