package com.spring.jwt.vender;

import com.spring.jwt.MapperClasses.VendorMapper;
import com.spring.jwt.exception.DataIntegrityViolationException;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.exception.UserNotFoundExceptions;
import com.spring.jwt.utils.BaseResponseDTO;
import com.spring.jwt.utils.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService{

    private final VendorRepository vendorRepository;

    private final VendorMapper vendorMapper;

    @Override
    public BaseResponseDTO register(VendorDto vendorDto) {
        Optional<Vendor> byPanNo = vendorRepository.findByPanNo(vendorDto.getPanNo());
        if (!byPanNo.isEmpty()) {
            throw new DataIntegrityViolationException("A vendor with the same Pan Number already exists. Please use a different value." +
                    vendorDto.getPanNo());
        }
        vendorRepository.save(vendorMapper.toEntity(vendorDto));
        return new BaseResponseDTO("201","Vendor registered successfully");
    }

    @Override
    public ResponseEntity<?> getUserById(Integer vendorId) {
       return ResponseEntity.ok(
               vendorRepository.findById(vendorId)
                       .map(vendorMapper::toDto)
       );
    }

    @Override
    public ResponseEntity<List<VendorDto>> getAllVendors() {
        List<VendorDto> vendorList = vendorRepository.findAll()
                .stream()
                .map(vendorMapper::toDto)
                .collect(Collectors.toList());
                 return ResponseEntity.ok(vendorList);
    }

    @Override
    public ResponseEntity<VendorDto> UpdateVendor(Integer vendorId, VendorDto vendorDto) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new RuntimeException("Vendor Not Found"));

        if (vendorDto.getName() != null) {
            vendor.setName(vendorDto.getName());
        }
        if (vendorDto.getGSTno() != null) {
            vendor.setGSTno(vendorDto.getGSTno());
        }
        if (vendorDto.getAddress() != null) {
            vendor.setAddress(vendorDto.getAddress());
        }
        if (vendorDto.getSpareBrand() != null) {
            vendor.setSpareBrand(vendorDto.getSpareBrand());
        }
        if (vendorDto.getMobileNumber() != null) {
            vendor.setMobileNumber(vendorDto.getMobileNumber());
        }
        if (vendorDto.getPanNo() != null) {
            vendor.setPanNo(vendorDto.getPanNo());
        }

        Vendor updatedVendor = vendorRepository.save(vendor);
        return ResponseEntity.ok(vendorMapper.toDto(updatedVendor));
    }

    @Override
    public void deleteVendorById(Integer vendorId) {
        if (!vendorRepository.existsById(vendorId)) {
            throw new UserNotFoundExceptions("Vendor not found with id: " + vendorId);
        }
        vendorRepository.deleteById(vendorId);
    }

}
