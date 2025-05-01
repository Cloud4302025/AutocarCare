package com.spring.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManageNoteDto {
    private Long manageNoteId;
    private String selectNoteOn;
    private String writeNote;
}
