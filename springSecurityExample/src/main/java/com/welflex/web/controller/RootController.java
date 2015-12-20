package com.welflex.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootController {
  @RequestMapping("/")
  public String root() {
    return "home";
  }
  
  @RequestMapping("/home.html")
  public String toHome() {
    return "home";
  }
}
