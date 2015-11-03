package com.welflex.spring.web.notes.service;

import com.welflex.spring.web.notes.Note;

import javax.inject.Named;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Named
public class NotesServiceImpl implements NotesService {
  private final ConcurrentHashMap<Long, Note> noteMap = new ConcurrentHashMap<Long, Note>();
  private AtomicLong noteIdGenerator = new AtomicLong();

  public NotesServiceImpl() {
    Long welcomeId = noteIdGenerator.getAndIncrement();
    noteMap.put(welcomeId, new Note("Welcome Note"));
  }

  public Long createNote(Note note) {
    Long id  = noteIdGenerator.getAndIncrement();
    noteMap.put(id, note);
    return id;
  }

  public Note getNote(Long id) {
    return noteMap.get(id);
  }
}
