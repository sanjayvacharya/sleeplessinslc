package com.welflex.spring;

import org.jamon.Renderer;
import org.springframework.web.servlet.ModelAndView;

public class JamonModelAndView extends ModelAndView {  
  
  public JamonModelAndView(Renderer renderer) {
    super(new JamonView(renderer));      
  }
  
  public JamonModelAndView(JamonView view) {
    super(view);
  }
  
  // Probably want to ensure that other methods are not invokable by throwing UnsupportedOperationException
  // Sadness that ModelAndView is not an interface or one could use dynamic proxies!
}
