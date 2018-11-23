package com.welflex.notes;

import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration
    implements WebMvcConfigurer, WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

  @Override
  public void customize(ConfigurableServletWebServerFactory factory) {
    MimeMappings mimeMappings = new MimeMappings(MimeMappings.DEFAULT);
    mimeMappings.add("yaml", "text/vnd.yaml");
    factory.setMimeMappings(mimeMappings);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/api.yaml").addResourceLocations("classpath:/api.yaml");
  }
}
