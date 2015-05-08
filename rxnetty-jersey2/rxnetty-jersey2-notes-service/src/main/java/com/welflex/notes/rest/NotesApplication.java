package com.welflex.notes.rest;

import java.util.Set;

import javax.ws.rs.core.Application;

import com.google.common.collect.ImmutableSet;

public class NotesApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    return ImmutableSet.<Class<?>> of(NotesResource.class, HealthResource.class, NoteNotFoundExceptionMapper.class,
      HealthResource.class);
  }
}
