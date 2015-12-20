package com.welflex.notes.jaxrs.client;

import java.util.List;

import javax.ws.rs.core.Form;

import com.welflex.notes.exception.NoteNotFoundException;
import com.welflex.notes.jaxb.Note;
import com.welflex.notes.jaxb.NoteResult;

/**
 * A Notes Client Contract
 */
public interface NotesClient  {
  NoteResult create(Note note, String role);

  List<Note> getAll();

  void update(Long noteId, Note note);

  Note get(Long noteId) throws NoteNotFoundException;

  void delete(Long noteId);
  
  String create(Form form);
}
