package com.welflex.notes.jaxb;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
  public Note createNote() {
    return new Note();
  }
  
  public NoteResult createNoteResult() {
    return new NoteResult();
  }
  
  public NotesResultContainer createNoteResultContainer() {
    return new NotesResultContainer();
  }
}

