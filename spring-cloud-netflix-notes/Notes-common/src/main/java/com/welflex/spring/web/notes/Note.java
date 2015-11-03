package com.welflex.spring.web.notes;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Note {

  private String content;

  public Note() {}

  public Note(String content) {
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Note note = (Note) o;

    return !(content != null ? !content.equals(note.content) : note.content != null);

  }

  @Override
  public int hashCode() {
    return content != null ? content.hashCode() : 0;
  }
}
