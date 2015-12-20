package com.welflex.notes;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.google.common.collect.ImmutableSet;
import com.welflex.notes.rest.LoggingFilter;
import com.welflex.notes.rest.NoteNotFoundExceptionMapper;
import com.welflex.notes.rest.resource.HealthResource;
import com.welflex.notes.rest.resource.NotesResource;
import com.welflex.notes.spring.security.AccessDeniedExeptionMapper;

public class NotesApplication extends Application {
  
  @Override
  public Set<Class<?>> getClasses() {
    return ImmutableSet.<Class<?>>of(NotesResource.class, 
      HealthResource.class,
      NoteNotFoundExceptionMapper.class, LoggingFilter.class, AccessDeniedExeptionMapper.class);
  }
}
