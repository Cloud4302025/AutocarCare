package com.spring.jwt.repository;

import com.spring.jwt.entity.ManageNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManageNoteRepository extends JpaRepository<ManageNote, Long> {
}
