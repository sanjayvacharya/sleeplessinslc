package com.welflex.notes.jaxrs.client;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import rx.Observable;

import com.welflex.notes.exception.NoteNotFoundException;
import com.welflex.notes.jaxb.Note;
import com.welflex.notes.jaxb.NoteResult;

/**
 * A Notes Client Contract
 */
public interface NotesClient  {
  Observable<NoteResult> create(Note note);
  
  Observable<Note> allNotes();

  Observable<Response> update(Long noteId, Note note);

  Observable<Note> get(Long noteId) throws NoteNotFoundException;

  Observable<Response> delete(Long noteId);
  
  Observable<String> create(Form form);
  
  Observable<Response> isHealthy();
}
