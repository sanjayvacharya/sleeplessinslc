package com.welflex.spring.web.notes.service;

import com.welflex.spring.web.notes.Note;

public interface NotesService {

  Long createNote(Note note);

  Note getNote(Long id);
}
