package com.welflex.notes.jaxb;

import java.net.URI;

import javax.xml.bind.annotation.XmlRootElement;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@XmlRootElement
@AutoProperty
public class NoteResult {
  private Long noteId;

  private URI location;


  public NoteResult() {}

  public NoteResult(Long noteId, URI location) {
    this.noteId = noteId;
    this.location = location;
  }

  public void setLocation(URI location) {
    this.location = location;
  }

  public URI getLocation() {
    return location;
  }

  public void setNoteId(Long noteId) {
    this.noteId = noteId;
  }

  public Long getNoteId() {
    return noteId;
  }

  @Override
  public String toString() {
    return Pojomatic.toString(this);
  }

  @Override
  public boolean equals(Object other) {
    return Pojomatic.equals(this, other);
  }

  @Override
  public int hashCode() {
    return Pojomatic.hashCode(this);
  }
}
