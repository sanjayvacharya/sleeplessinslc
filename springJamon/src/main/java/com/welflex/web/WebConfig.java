package com.welflex.web;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.welflex.spring.JamonTemplateHttpConverter;
import com.welflex.web.controller.ControllerConfig;

@EnableWebMvc
@Configuration
@Import({ControllerConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter {
  
  @Override
  public Validator getValidator() {
    return new LocalValidatorFactoryBean();
  }
  
  /**
   * Tell Spring about the JamonTemplateHttpConverter
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new JamonTemplateHttpConverter());
  }

  @Override
  public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
  }
}
