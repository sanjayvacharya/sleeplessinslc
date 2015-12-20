package com.welflex.notes.rest.resource;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;

import com.welflex.notes.jaxb.Note;
import com.welflex.notes.jaxb.NoteResult;
import com.welflex.notes.rest.Loggable;
import com.welflex.service.NotesService;

@Path("/notes")
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
public class NotesResource {
  private static final Logger LOG = Logger.getLogger(NotesResource.class);

  private final UriInfo uriInfo;

  private final NotesService notesService;
  
  @Inject
  public NotesResource(@Context UriInfo uriInfo, NotesService notesService) {
    this.uriInfo = uriInfo;
    this.notesService = notesService;
  }
  
  @POST
  @Loggable
  @PreAuthorize("hasRole('notesuser')")
  public Response create(Note note) {
    LOG.debug("Creating a note:" + note);
    NoteResult result = createNote(note);
    
    return Response.created(result.getLocation()).entity(result).build();
  }
  
  private NoteResult createNote(Note note) {
    Long noteId = notesService.create(note);
   
    URI createdLocation = UriBuilder.fromPath(uriInfo.getAbsolutePath() + "/" + noteId).build();
    NoteResult result = new NoteResult(noteId, createdLocation);

    return result;
  }
  
  // Validation does not seem to be working at present.
  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  public Response create(@NotNull @FormParam("userId") String userId,
    @NotNull @FormParam("content") String content) {
    NoteResult result = createNote(new Note(userId, content));
    
    return Response.created(result.getLocation()).entity("<html><body><b>Note created:" + result.getNoteId() + "</b></body></html>").type(MediaType.TEXT_HTML).build();
  }
 
  @GET
  public List<Note> getNotes() {
    return notesService.getAll();
  }
  
  @Path("/{noteId}")
  public NoteResource noteResource(@Context ResourceContext context) {
    return context.getResource(NoteResource.class);
  }
}
