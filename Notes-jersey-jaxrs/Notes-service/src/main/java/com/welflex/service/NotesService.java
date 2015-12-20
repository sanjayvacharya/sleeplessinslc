package com.welflex.service;

import java.util.List;

import com.welflex.notes.jaxb.Note;

public interface NotesService {
  Long create(Note note);

  Note get(Long id);

  List<Note> getAll();

  void update(Long id, Note note);

  void delete(Long noteId);
}
