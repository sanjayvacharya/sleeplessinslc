package com.welflex.notes.client;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.google.common.collect.Lists;
import com.welflex.notes.jaxb.Note;
import com.welflex.notes.jaxb.NoteResult;


public class Main {
  private static final URI BASE_URI = URI.create("http://localhost:8080");

  public static class NotesApplication  extends ResourceConfig  {
    public NotesApplication() {
      super(NotesResource.class);
    }
  }
  
  @Path("/notes")
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  static class NotesResource {
    @Inject
    UriInfo uriInfo;
    
    public Response create(Note note) {
      URI createdLocation = UriBuilder.fromPath(uriInfo.getAbsolutePath() + "/" + 1L).build();
      NoteResult result = new NoteResult(1L, createdLocation);

      return Response.created(createdLocation).entity(result).build();
    }

    @GET
    public List<Note> getNotes() {
      return Lists.newArrayList(new Note("foo", "some note"));
    }

    @Path("/{noteId}")
    @GET
    public Note getNote(@PathParam("noteId") Long noteId) {
      return new Note("foo", "some note");
    }
   
    
    @Path("/{noteId}")
    @PUT
    public void update(@PathParam("noteId") Long noteId, Note note) {
      // NO op
    }
    
    @Path("/{noteId}")
    @DELETE
    public void delete(@PathParam("noteId") Long noteId) {
      // No op
    }
  }

  
  /**
   * Demonstrates starting of a Web Server using a Main method. This is akin to how one would test a
   * RESTful application started using Grizzly.
   */
  public static void main(String args[]) throws Exception { 
    final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, new NotesApplication());
    server.start();
    System.out.println(String.format("Application started.%nTry out %s%nHit enter to stop it...",
            BASE_URI));
    System.in.read();
    server.shutdown();
  }
}
