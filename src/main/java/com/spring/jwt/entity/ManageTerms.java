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
@Table(name = "ManageTerms")
public class ManageTerms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manageTermsId")
    private Long manageTermsId;

    private String selectNoteOn;

    private String writeTerms;

}
