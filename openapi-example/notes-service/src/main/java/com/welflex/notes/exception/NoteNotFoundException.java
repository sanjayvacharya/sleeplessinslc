package com.welflex.notes.exception;

public class NoteNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final Integer noteId;
  
  public NoteNotFoundException(Integer noteId) {
    this(noteId, "Not found");
  }

  public NoteNotFoundException(Integer noteId, String message) {
    super(message);
    this.noteId = noteId;
  }

  public Integer getNoteId() {
    return noteId;
  }

}
