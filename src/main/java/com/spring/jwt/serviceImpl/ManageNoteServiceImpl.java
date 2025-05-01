package com.spring.jwt.serviceImpl;
import com.spring.jwt.dto.ManageNoteDto;
import com.spring.jwt.entity.ManageNote;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.ManageNoteRepository;
import com.spring.jwt.service.ManageNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManageNoteServiceImpl implements ManageNoteService {

    @Autowired
    private ManageNoteRepository repository;

    @Override
    public ManageNoteDto createNote(ManageNoteDto dto) {
        ManageNote note = ManageNote.builder()
                .selectNoteOn(dto.getSelectNoteOn())
                .writeNote(dto.getWriteNote())
                .build();
        return convertToDto(repository.save(note));
    }

    @Override
    public List<ManageNoteDto> getAllNotes() {
        return repository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ManageNoteDto getNoteById(Long id) {
        ManageNote note = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with ID: " + id));
        return convertToDto(note);
    }

    @Override
    public ManageNoteDto updateNote(Long id, ManageNoteDto dto) {
        ManageNote existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with ID: " + id));

        existing.setSelectNoteOn(dto.getSelectNoteOn());
        existing.setWriteNote(dto.getWriteNote());

        return convertToDto(repository.save(existing));
    }

    @Override
    public String deleteNote(Long id) {
        ManageNote note = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with ID: " + id));
        repository.delete(note);
        return "Note deleted successfully";
    }

    private ManageNoteDto convertToDto(ManageNote note) {
        return new ManageNoteDto(note.getManageNoteId(), note.getSelectNoteOn(), note.getWriteNote());
    }
}

