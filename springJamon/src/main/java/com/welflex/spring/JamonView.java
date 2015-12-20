package com.welflex.spring;

import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jamon.Renderer;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;

public class JamonView implements View {
  private final Renderer renderer;
  
  public JamonView(Renderer renderer) {
    this.renderer = renderer;
  }
  
  @Override
  public String getContentType() {
    return MediaType.TEXT_HTML_VALUE;
  }

  @Override
  public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {  
    StringWriter writer = new StringWriter();
    renderer.renderTo(writer);
    response.getOutputStream().write(writer.toString().getBytes());
  }
}
