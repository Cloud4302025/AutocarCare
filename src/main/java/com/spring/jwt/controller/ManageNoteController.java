package com.spring.jwt.controller;

import com.spring.jwt.Appointment.ResponseDto;
import com.spring.jwt.dto.ManageNoteDto;
import com.spring.jwt.service.ManageNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manageNotes")
public class ManageNoteController {

    @Autowired
    private ManageNoteService service;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<ManageNoteDto>> create(@RequestBody ManageNoteDto dto) {
        try {
            ManageNoteDto created = service.createNote(dto);
            return ResponseEntity.ok(ResponseDto.success("Note created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseDto.error("Failed to create note", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDto<List<ManageNoteDto>>> getAll() {
        try {
            List<ManageNoteDto> notes = service.getAllNotes();
            return ResponseEntity.ok(ResponseDto.success("All notes fetched", notes));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseDto.error("Failed to fetch notes", e.getMessage()));
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<ResponseDto<ManageNoteDto>> getById(@RequestParam Long id) {
        try {
            ManageNoteDto note = service.getNoteById(id);
            return ResponseEntity.ok(ResponseDto.success("Note fetched successfully", note));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(ResponseDto.error("Note not found", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto<ManageNoteDto>> update(
            @RequestParam Long id,
            @RequestBody ManageNoteDto dto) {
        try {
            ManageNoteDto updated = service.updateNote(id, dto);
            return ResponseEntity.ok(ResponseDto.success("Note updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(ResponseDto.error("Failed to update note", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<String>> delete(@RequestParam Long id) {
        try {
            String msg = service.deleteNote(id);
            return ResponseEntity.ok(ResponseDto.success("Note deleted", msg));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(ResponseDto.error("Failed to delete note", e.getMessage()));
        }
    }
}
