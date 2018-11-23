package com.welflex.notes.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.welflex.notes.model.NoteModel;

@Repository
public interface NotesRepository extends PagingAndSortingRepository<NoteModel, Integer> {
}
