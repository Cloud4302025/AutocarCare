package com.spring.jwt.FilterController;

import com.spring.jwt.SparePart.SpareFilterDto;
import com.spring.jwt.SparePart.SparePart;
import com.spring.jwt.SparePart.SparePartDto;
import com.spring.jwt.UserParts.UserPartDto;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface FilterService {
    List<SparePartDto> searchBarFilter(String searchBarInput);

    public List<SpareFilterDto> searchSpareParts(String keyword, int limit);

    List<String> getAllManufacturers();

    List<UserPartDto> searchBarFilterUserPart(String searchBarInput);
}
