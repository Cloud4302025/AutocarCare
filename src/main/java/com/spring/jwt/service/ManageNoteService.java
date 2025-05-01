package com.spring.jwt.service;

import com.spring.jwt.dto.ManageNoteDto;

import java.util.List;

public interface ManageNoteService {
    ManageNoteDto createNote(ManageNoteDto dto);

    List<ManageNoteDto> getAllNotes();

    ManageNoteDto getNoteById(Long id);

    ManageNoteDto updateNote(Long id, ManageNoteDto dto);

    String deleteNote(Long id);
}
