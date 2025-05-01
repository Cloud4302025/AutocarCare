package com.spring.jwt.service;

import com.spring.jwt.dto.ManageTermsDto;
import java.util.List;

public interface ManageTermsService {
    ManageTermsDto createTerms(ManageTermsDto dto);
    ManageTermsDto getTermsById(Long id);
    List<ManageTermsDto> getAllTerms();
    ManageTermsDto updateTerms(Long id, ManageTermsDto dto);
    String deleteTerms(Long id);
}
