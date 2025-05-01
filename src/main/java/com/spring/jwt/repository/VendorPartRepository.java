package com.spring.jwt.repository;

import com.spring.jwt.entity.VendorPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorPartRepository extends JpaRepository<VendorPart, Integer> {
    Optional<VendorPart> findByVendorAndPartNumber(String vendor, String partNumber);

    List<VendorPart> findByManufacturerAndPartNumber(
            String manufacturer,
            String partNumber
    );}
