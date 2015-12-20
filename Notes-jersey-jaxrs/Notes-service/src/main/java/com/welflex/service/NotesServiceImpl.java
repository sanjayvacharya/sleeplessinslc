package com.welflex.service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Lists;
import com.welflex.notes.jaxb.Note;

public class NotesServiceImpl implements NotesService {
  private final ConcurrentHashMap<Long, Note> noteMap = new ConcurrentHashMap<Long, Note>();
  private final AtomicLong seq = new AtomicLong();
  
  public NotesServiceImpl() {
  }
  
  @Override
  public Long create(Note note) {
    Long noteId = seq.incrementAndGet();
    noteMap.put(noteId, note);  
    
    return noteId;
  }
  
  @Override
  public Note get(Long noteId) {
    return noteMap.get(noteId);
  }

  @Override
  public void update(Long id, Note note) {
    noteMap.put(id, note);
  }

  @Override
  public void delete(Long noteId) {
    noteMap.remove(noteId);
  }

  @Override
  public List<Note> getAll() {
    return Lists.newArrayList(noteMap.values());
  }  
}