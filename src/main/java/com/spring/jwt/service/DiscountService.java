package com.spring.jwt.service;

import com.spring.jwt.dto.DiscountStructureDTO;
import io.micrometer.observation.ObservationFilter;

import java.util.List;
import java.util.Optional;

public interface DiscountService {
    void addDiscount(DiscountStructureDTO dto);
    Optional<DiscountStructureDTO> getById(Integer id);
    List<DiscountStructureDTO> getAll();
    DiscountStructureDTO updateDiscount(Integer id, DiscountStructureDTO dto);
    void deleteDiscount(Integer id);

    Optional<DiscountStructureDTO> getByManufacturer(String manufacturer);

}
