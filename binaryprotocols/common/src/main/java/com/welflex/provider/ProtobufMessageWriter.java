package com.welflex.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.google.protobuf.Message;

/**
 * A Message Body Writer
 */
@Provider
@Component
@Produces(AlternateMediaType.APPLICATION_XPROTOBUF)
public class ProtobufMessageWriter implements MessageBodyWriter<Message> {
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
    MediaType mediaType) {
    return Message.class.isAssignableFrom(type);
  }

  public long getSize(Message m, Class<?> type, Type genericType, Annotation[] annotations,
    MediaType mediaType) {
    return m.getSerializedSize();
  }

  public void writeTo(Message m, Class<?> type, Type genericType, Annotation[] annotations,
    MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
    WebApplicationException {
    entityStream.write(m.toByteArray());
  }
}
