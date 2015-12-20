package com.welflex.notes.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.google.common.collect.Lists;

@XmlRootElement
@AutoProperty
public class NotesResultContainer {
  public List<NoteResult> getNoteResult() {
    return noteResult;
  }

  public void setNoteResult(List<NoteResult> noteResult) {
    this.noteResult = noteResult;
  }

  public void addNoteResult(NoteResult result) {
    noteResult.add(result);
  }

  private List<NoteResult> noteResult = Lists.newArrayList();

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
