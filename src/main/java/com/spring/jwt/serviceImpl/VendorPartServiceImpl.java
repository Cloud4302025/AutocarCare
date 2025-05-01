package com.spring.jwt.serviceImpl;


import com.spring.jwt.MapperClasses.VendorMapper;
import com.spring.jwt.dto.VendorPartDto;
import com.spring.jwt.entity.VendorPart;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.VendorPartRepository;
import com.spring.jwt.service.VendorPartService;
import com.spring.jwt.vender.Vendor;
import com.spring.jwt.vender.VendorDto;
import com.spring.jwt.vender.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VendorPartServiceImpl implements VendorPartService {

    @Autowired
    private VendorPartRepository vendorPartRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired

    private VendorMapper vendorMapper;
    @Override
    public VendorPartDto create(VendorPartDto vendorPartDto) {
        VendorPart vendorPart = mapToEntity(vendorPartDto);
        VendorPart saved = vendorPartRepository.save(vendorPart);
        return mapToDto(saved);
    }

    @Override
    public VendorPartDto update(Integer id, VendorPartDto dto) {
        VendorPart existing = vendorPartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VendorPart not found with ID: " + id));

        existing.setSparePartId(dto.getSparePartId());
        existing.setPartName(dto.getPartName());
        existing.setDescription(dto.getDescription());
        existing.setManufacturer(dto.getManufacturer());
        existing.setPartNumber(dto.getPartNumber());
        existing.setVendor(dto.getVendor());

        VendorPart updated = vendorPartRepository.save(existing);
        return mapToDto(updated);
    }


    @Override
    public VendorPartDto getById(Integer id) {
        VendorPart vendorPart = vendorPartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VendorPart not found with id: " + id));
        return mapToDto(vendorPart);
    }

    @Override
    public List<VendorPartDto> getAll() {
        List<VendorPart> list = vendorPartRepository.findAll();
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        VendorPart vendorPart = vendorPartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VendorPart not found with id: " + id));
        vendorPartRepository.delete(vendorPart);
    }

    @Override
    public List<VendorPartDto> getByPartNumber(String partNumber, String manufacturer) {
        List<VendorPart> parts = vendorPartRepository.findByManufacturerAndPartNumber(partNumber, manufacturer);
        if (parts.isEmpty()) {
            throw new ResourceNotFoundException("No VendorParts found with part number: " + partNumber);
        }
        return parts.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private VendorPartDto mapToDto(VendorPart entity) {
        return VendorPartDto.builder()
                .vendorPartId(entity.getVendorPartId())
                .sparePartId(entity.getSparePartId())
                .partName(entity.getPartName())
                .description(entity.getDescription())
                .manufacturer(entity.getManufacturer())
                .partNumber(entity.getPartNumber())
                .vendor(entity.getVendor())
                .vendorId(entity.getVendorId())
                .vendorId(entity.getVendorId())
                .build();
    }

    private VendorPart mapToEntity(VendorPartDto dto) {
        return VendorPart.builder()
                .vendorPartId(dto.getVendorPartId())
                .sparePartId(dto.getSparePartId())
                .partName(dto.getPartName())
                .vendorId(dto.getVendorId())
                .description(dto.getDescription())
                .manufacturer(dto.getManufacturer())
                .partNumber(dto.getPartNumber())
                .vendor(dto.getVendor())
                .build();
    }

    @Override
    public List<VendorDto> getVendorsByPartNumberAndManufacturer(
            String partNumber,
            String manufacturer
    ) {
        // fetch parts by (manufacturer, partNumber)
        List<VendorPart> parts = vendorPartRepository
                .findByManufacturerAndPartNumber(manufacturer, partNumber);

        if (parts.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No VendorParts found for partNumber="
                            + partNumber
                            + ", manufacturer="
                            + manufacturer
            );
        }

        // extract distinct vendor IDs
        Set<Integer> vendorIds = parts.stream()
                .map(VendorPart::getVendorId)
                .collect(Collectors.toSet());

        // load vendor entities in one shot
        List<Vendor> vendors = vendorRepository.findAllById(vendorIds);

        if (vendors.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Vendors found for vendorIds=" + vendorIds
            );
        }

        // map to DTOs
        return vendors.stream()
                .map(vendorMapper::toDto)
                .collect(Collectors.toList());
    }
}
