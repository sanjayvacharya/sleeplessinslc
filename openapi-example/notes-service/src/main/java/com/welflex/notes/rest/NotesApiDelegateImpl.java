package com.welflex.notes.rest;

import java.net.URI;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.welflex.notes.api.generated.Note;
import com.welflex.notes.api.generated.Notes;
import com.welflex.notes.api.generated.NotesApi;
import com.welflex.notes.api.generated.NotesApiDelegate;
import com.welflex.notes.api.generated.NotesPage;
import com.welflex.notes.api.generated.PageMetadata;
import com.welflex.notes.exception.NoteNotFoundException;
import com.welflex.notes.model.NoteModel;
import com.welflex.notes.repository.NotesRepository;

@Component
public class NotesApiDelegateImpl implements NotesApiDelegate {

  private final NotesRepository notesRepository;
  
  @Autowired
  public NotesApiDelegateImpl(NotesRepository notesRepository) {
    this.notesRepository = notesRepository;
  }
  
  private Note toNote(NoteModel noteModel) {
    return new Note().noteId(noteModel.getId()).content(noteModel.getContent());
  }
  

  private NoteModel toNoteModel(Note note) {
    return new NoteModel(note.getNoteId(), note.getContent());
  }
  
  /**
   * @see NotesApi#createNote
   */
  public ResponseEntity<Note> createNote( Note  note) {
    NoteModel noteModel = toNoteModel(note);
    noteModel = notesRepository.save(noteModel);
    URI uri= ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(noteModel.getId()).toUri();
    
    return ResponseEntity.created(uri).body(toNote(noteModel));
  }

  /**
   * @see NotesApi#getNote
   */
  public ResponseEntity<Note> getNote( Integer  id) {

    Optional<NoteModel> noteModel = notesRepository.findById(id);
   
    if (noteModel.isPresent()) {
      return ResponseEntity.ok(toNote(noteModel.get()));
    }
    
    throw new NoteNotFoundException(id);
  }

  /**
   * @see NotesApi#getNotes
   */
  public ResponseEntity<NotesPage> getNotes( Integer  page,
       Integer  pageSize) {
  
    int pageRequest = page == null? 0 : page;
    int limitRequested = pageSize == null ? 100 : pageSize;
    
    Page<NoteModel> dataPage = notesRepository.findAll(PageRequest.of(pageRequest, limitRequested));
    
    PageMetadata pageMetadata = new PageMetadata().pageNumber(pageRequest).pageSize(limitRequested).resultCount(dataPage.getNumberOfElements())
        .totalResults(dataPage.getTotalElements());
    
    Notes notes = new Notes();
    
    notes.addAll(dataPage.stream().map(t -> toNote(t)).collect(Collectors.toList()));
    
    NotesPage notesPage = new NotesPage().items(notes).metadata(pageMetadata);
    
    return ResponseEntity.<NotesPage>ok(notesPage);
  }

  /**
   * @see NotesApi#updateNote
   */
  public ResponseEntity<Void> updateNote( Note  note, Integer  id) {
    NoteModel noteModel = toNoteModel(note);
    
    notesRepository.save(noteModel);

    return ResponseEntity.noContent().build();
  }
  
  @Override
  public ResponseEntity<Void> deleteNote(Integer  id) {
    notesRepository.deleteById(id);    
    return ResponseEntity.noContent().build();
  }  
}
