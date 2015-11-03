package com.welflex.spring.web.notes.controller;

import com.welflex.spring.web.notes.Note;
import com.welflex.spring.web.notes.service.NotesService;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@Api(basePath = "/notes", value = "Notes", description = "Note Creation and Retrieval", produces = "application/xml")
public class NotesController {
  @Inject
  private NotesService notesService;

  @RequestMapping(value = "/notes", method = RequestMethod.POST)
  public Long create(@RequestBody Note note) {
    return notesService.createNote(note);
  }

  @RequestMapping(value = "/notes/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
  public Note get(@PathVariable Long id) {
    return notesService.getNote(id);
  }
}
