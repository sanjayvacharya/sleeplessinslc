package com.welflex.notes.client;

import com.welflex.spring.web.notes.Note;

public interface NotesClient {

  Note getNote(Long noteId);

  Long createNote(Note note);
}
