package com.weflex.example.notes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import com.welflex.notes.api.ApiClient;
import com.welflex.notes.api.generated.Note;
import com.welflex.notes.api.generated.NotesApi;

//https://www.testcontainers.org/usage/generic_containers.html#accessing-a-container-from-tests
public class NotesIntegrationTest {
  
  @ClassRule
  public static GenericContainer<?> notesService =
      new FixedHostPortGenericContainer<>("notes:latest").withExposedPorts(8080);
    
  
  /**
   * @return NotesApi client using RestTemplate
   */
  private NotesApi createNotesApiClient() {
    // One could configure this Apache HTTP Client if needed
    RestTemplate template = new RestTemplate();
    ApiClient apiClient = new ApiClient(template)
         .setBasePath(String.format("http://%s:%d", notesService.getContainerIpAddress(), notesService.getMappedPort(8080)));
    
    apiClient.setUsername("user");
    apiClient.setPassword("password");

    return new NotesApi(apiClient);
  }
  
  @Test
  public void integration() {    
    NotesApi notesApiClient = createNotesApiClient();
    
    // Get all notes
    assertTrue(notesApiClient.getNotes(0, 100).getItems().size() > 0);
    
    // Create
    Note note = new Note();
    note.setContent("Hello World!");
    
    Note createdNote = notesApiClient.createNote(note);
    
    assertEquals("Hello World!", createdNote.getContent());
    
    // Read
    Integer noteId = createdNote.getNoteId();
    
    note = notesApiClient.getNote(noteId);

    assertEquals(createdNote, note);
    
    // Update
    note.setContent("Hello Wonderful World!");
    notesApiClient.updateNote(note, noteId);
    
    // Read Again and validate the update
    note = notesApiClient.getNote(noteId);
    assertEquals("Hello Wonderful World!", note.getContent());
    
    // Delete note
    notesApiClient.deleteNote(noteId);
    
    try {
      note = notesApiClient.getNote(noteId);
      fail("This should not have happened");
    }
    catch (HttpClientErrorException expected) {
      assertEquals(HttpStatus.NOT_FOUND, expected.getStatusCode());
    }
  }

}
