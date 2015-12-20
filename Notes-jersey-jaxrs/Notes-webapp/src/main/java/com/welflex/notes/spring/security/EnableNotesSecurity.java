package com.welflex.notes.spring.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Import(NotesSecurityConfig.class)
public @interface EnableNotesSecurity {
}
