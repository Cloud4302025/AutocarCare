package com.spring.jwt.repository;

import com.spring.jwt.entity.DiscountStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountStructureRepository
        extends JpaRepository<DiscountStructure, Integer> {

    Optional<DiscountStructure> findByManufacturer(String manufacturer);

    boolean existsByManufacturer(String manufacturer);

    Optional<DiscountStructure> findByManufacturerIgnoreCase(String manufacturer);

}
