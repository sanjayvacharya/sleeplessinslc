package com.welflex.web.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

  @RequestMapping(value = "/login.html", method = RequestMethod.GET)
  public String loginView(@RequestParam(required=false) String login_error, ModelMap modelMap) {
    if (StringUtils.isNotBlank(login_error)) {
      modelMap.addAttribute("error", "true");
    }
    return "login";
  }
  
  @RequestMapping(value="/logoutSuccess.html", method=RequestMethod.GET)
  public String logoutView() {
    return "logoutSuccess";
  }
}
