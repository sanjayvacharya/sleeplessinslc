package com.welflex.web.controller;

import java.util.Map;

import javax.validation.Valid;

import org.jamon.Renderer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.overstock.web.LoginView;
import com.overstock.web.WelcomeView;
import com.welflex.model.User;
import com.welflex.spring.JamonModelAndView;

@Controller
public class LoginController {
  
  @RequestMapping("/")
  public String rootController() {
    return "redirect:/login.html";
  }
  
  @RequestMapping(value = "/login.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
  @ResponseBody
  public Renderer login() {
    return new LoginView().makeRenderer(new User(), null);
  }
  
  @RequestMapping(value="/login.html", method = RequestMethod.POST)
  public ModelAndView login(@Valid User user,  BindingResult result,  Map<String, Object> model) {
    return result.hasErrors() ?  new JamonModelAndView(new LoginView().makeRenderer(user, result))
      : new ModelAndView("redirect:/welcome.html");    
  }
  
  @RequestMapping(value="/welcome.html")
  public ModelAndView welcome() {
    return new JamonModelAndView(new WelcomeView().makeRenderer());
  }
}
