package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ManageNote")
public class ManageNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manageNoteId")
    private Long manageNoteId;

    private String selectNoteOn;

    private String writeNote;

}
