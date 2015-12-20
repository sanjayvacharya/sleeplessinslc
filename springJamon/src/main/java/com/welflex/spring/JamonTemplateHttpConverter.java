package com.welflex.spring;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.jamon.Renderer;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class JamonTemplateHttpConverter extends AbstractHttpMessageConverter<Renderer> {

  @Override
  protected boolean supports(Class<?> clazz) {
    return Renderer.class.isAssignableFrom(clazz);
  }
  
  @Override
  public boolean canWrite(Class<?> clazz, MediaType mediaType) {
    return supports(clazz) && MediaType.TEXT_HTML.equals(mediaType);
  }

  @Override 
  protected Renderer readInternal(Class<? extends Renderer> clazz, HttpInputMessage inputMessage) throws IOException,
    HttpMessageNotReadableException {
    throw new UnsupportedOperationException();
  }

  @Override
  protected void writeInternal(Renderer t, HttpOutputMessage outputMessage) throws IOException,
    HttpMessageNotWritableException {
    Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
    t.renderTo(new OutputStreamWriter(outputMessage.getBody(), charset));
  }
  
  private static final Charset DEFAULT_CHARSET = Charset.forName("ISO-8859-1");
  
  private Charset getContentTypeCharset(MediaType contentType) {
    if (contentType != null && contentType.getCharSet() != null) {
      return contentType.getCharSet();
    }
    
    return DEFAULT_CHARSET;
  }

}
