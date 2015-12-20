package com.welflex.notes;

import org.springframework.context.annotation.Configuration;

import com.welflex.notes.spring.security.EnableNotesSecurity;
import com.welflex.service.EnableNotesService;

@Configuration
@EnableNotesService
@EnableNotesSecurity
public class SpringConfig {
}
