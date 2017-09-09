package com.welflex.example.productgateway.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckResource {
  @RequestMapping(value = "health") 
  @ResponseBody
  public String health() {
	return "OK";
  }
}
