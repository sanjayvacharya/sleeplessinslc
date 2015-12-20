package com.welflex.notes.jaxrs.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

import com.welflex.notes.exception.NoteNotFoundException;
import com.welflex.notes.jaxb.Note;
import com.welflex.notes.jaxb.NoteResult;

/**
 * Implementation of the Notes Client
 */
public class NotesClientImpl implements NotesClient {
  private final String baseUri;

  private final Client webServiceClient;

  /**
   * @param baseUri Server URI
   */
  public NotesClientImpl(String baseUri) {
    this.baseUri = baseUri;
    ClientConfig config = new ClientConfig();

    config.register(new LoggingFilter());
    webServiceClient = ClientBuilder.newClient(config);
  }

  @Override
  public NoteResult create(Note note, String role) {
    return webServiceClient.target(UriBuilder.fromUri(baseUri).path("/notes").build())
        .request(MediaType.APPLICATION_XML)
        .header("role", role)
        .post(Entity.entity(note, MediaType.APPLICATION_XML_TYPE), NoteResult.class);
  }

  @Override
  public String create(Form form) {
    return webServiceClient.target(UriBuilder.fromUri(baseUri).path("/notes").build())
        .request(MediaType.TEXT_HTML)
        .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
  }

  /**
   * Gets a note using the preferred representation type of JSON
   */
  @Override
  public Note get(Long id) throws NoteNotFoundException {
    Response response = webServiceClient
        .target(UriBuilder.fromUri(baseUri).path("/notes/" + id).build())       
        .request(MediaType.APPLICATION_JSON).get();

    if (response.getStatus() == Status.NOT_FOUND.getStatusCode()) {
      throw new NoteNotFoundException(response.readEntity(String.class));
    }

    return response.readEntity(Note.class);
  }

  @Override
  public void update(Long noteId, Note note) {
    webServiceClient.target(UriBuilder.fromUri(baseUri).path("/notes/" + noteId).build()).request()  
      .put(Entity.entity(note, MediaType.APPLICATION_XML));
  }

  @Override
  public void delete(Long noteId) {
    webServiceClient.target(UriBuilder.fromUri(baseUri).path("/notes/" + noteId).build()).request()
        .delete();
  }

  @Override
  public List<Note> getAll() {
    return webServiceClient.target(UriBuilder.fromUri(baseUri).path("/notes").build())        
        .request(MediaType.APPLICATION_XML).get(new GenericType<List<Note>>() {});
  }

}
