package com.spring.jwt.controller;

import com.spring.jwt.Appointment.ResponseDto;
import com.spring.jwt.dto.ManageTermsDto;
import com.spring.jwt.service.ManageTermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manageTerms")
public class ManageTermsController {

    @Autowired
    private ManageTermsService service;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<ManageTermsDto>> create(@RequestBody ManageTermsDto dto) {
        try {
            ManageTermsDto created = service.createTerms(dto);
            return ResponseEntity.ok(ResponseDto.success("Terms created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseDto.error("Failed to create terms", e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseDto<List<ManageTermsDto>>> getAll() {
        try {
            List<ManageTermsDto> terms = service.getAllTerms();
            return ResponseEntity.ok(ResponseDto.success("All terms fetched", terms));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ResponseDto.error("Failed to fetch terms", e.getMessage()));
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<ResponseDto<ManageTermsDto>> getById(@RequestParam Long id) {
        try {
            ManageTermsDto terms = service.getTermsById(id);
            return ResponseEntity.ok(ResponseDto.success("Terms fetched", terms));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(ResponseDto.error("Terms not found", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto<ManageTermsDto>> update(@RequestParam Long id, @RequestBody ManageTermsDto dto) {
        try {
            ManageTermsDto updated = service.updateTerms(id, dto);
            return ResponseEntity.ok(ResponseDto.success("Terms updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(ResponseDto.error("Failed to update terms", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto<String>> delete(@RequestParam Long id) {
        try {
            String msg = service.deleteTerms(id);
            return ResponseEntity.ok(ResponseDto.success("Terms deleted", msg));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(ResponseDto.error("Failed to delete terms", e.getMessage()));
        }
    }
}
