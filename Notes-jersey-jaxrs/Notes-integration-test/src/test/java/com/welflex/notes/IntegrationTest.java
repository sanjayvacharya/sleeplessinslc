package com.welflex.notes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import javax.ws.rs.ForbiddenException;

import org.junit.Before;
import org.junit.Test;

import com.welflex.notes.exception.NoteNotFoundException;
import com.welflex.notes.jaxb.Note;
import com.welflex.notes.jaxrs.client.NotesClient;
import com.welflex.notes.jaxrs.client.NotesClientImpl;

public class IntegrationTest {
  private NotesClient client;

  @Before
  public void before() throws Exception {
    client = new NotesClientImpl("http://localhost:9091/IntegrationTest");
  }
  
  @Test(expected = ForbiddenException.class)
  public void roleAbsent() {
    Note note = new Note();
    note.setUserId("sacharya");
    note.setContent("Something to say");
    client.create(note, "Fake Role").getNoteId();
    fail("Should not have created a note as the role provided is not supported");
  }

  @Test 
  public void noteLifeCycle() {
    Note note = new Note();
    note.setUserId("sacharya");
    note.setContent("Something to say");
    
    // Create
    Long noteId = null; 
    noteId = client.create(note, "notesuser").getNoteId();
    
    System.out.println("Created Note with ID:" + noteId);
    
    // Read
    Note fetched = client.get(noteId);
    assertEquals(note, fetched);
    
    // Read All
    List<Note> allNotes = client.getAll();
    assertTrue(allNotes.size() == 1);
    
    assertEquals(note, allNotes.get(0));
    
    // Update
    note.setContent("Something to say updated");
    client.update(noteId, note);
    
    // Fetch and verify update
    fetched = client.get(noteId);
    assertEquals(note, fetched);
    
    // Delete
    client.delete(noteId);
    
    // Verify deleted
    try {
      fetched = client.get(noteId); 
      fail("Should have thrown us a NoteNotFoundException");
    } catch (NoteNotFoundException expected) {}
  }
}
