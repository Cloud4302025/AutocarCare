package com.spring.jwt.serviceImpl;

import com.spring.jwt.dto.ManageTermsDto;
import com.spring.jwt.entity.ManageTerms;
import com.spring.jwt.repository.ManageTermsRepository;
import com.spring.jwt.service.ManageTermsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManageTermsServiceImpl implements ManageTermsService {

    @Autowired
    private ManageTermsRepository repository;

    private ManageTermsDto convertToDto(ManageTerms entity) {
        ManageTermsDto dto = new ManageTermsDto();
        dto.setManageTermsId(entity.getManageTermsId());
        dto.setSelectNoteOn(entity.getSelectNoteOn());
        dto.setWriteTerms(entity.getWriteTerms());
        return dto;
    }

    private ManageTerms convertToEntity(ManageTermsDto dto) {
        return ManageTerms.builder()
                .manageTermsId(dto.getManageTermsId())
                .selectNoteOn(dto.getSelectNoteOn())
                .writeTerms(dto.getWriteTerms())
                .build();
    }

    @Override
    public ManageTermsDto createTerms(ManageTermsDto dto) {
        ManageTerms saved = repository.save(convertToEntity(dto));
        return convertToDto(saved);
    }

    @Override
    public ManageTermsDto getTermsById(Long id) {
        ManageTerms entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terms not found with id: " + id));
        return convertToDto(entity);
    }

    @Override
    public List<ManageTermsDto> getAllTerms() {
        return repository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ManageTermsDto updateTerms(Long id, ManageTermsDto dto) {
        ManageTerms existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terms not found with id: " + id));
        existing.setSelectNoteOn(dto.getSelectNoteOn());
        existing.setWriteTerms(dto.getWriteTerms());
        return convertToDto(repository.save(existing));
    }

    @Override
    public String deleteTerms(Long id) {
        ManageTerms existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terms not found with id: " + id));
        repository.delete(existing);
        return "Deleted successfully";
    }
}
