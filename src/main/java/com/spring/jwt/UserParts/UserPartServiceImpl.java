package com.spring.jwt.UserParts;

import com.spring.jwt.SparePart.PaginatedResponse;
import com.spring.jwt.SparePart.SparePart;
import com.spring.jwt.SparePart.SparePartRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserPartServiceImpl implements UserPartService {

    private final UserPartRepository userPartRepository;

    private final UserPartMapper userPartMapper;

    private final SparePartRepo sparePartRepo;

    public UserPartServiceImpl(UserPartRepository userPartRepository, UserPartMapper userPartMapper, SparePartRepo sparePartRepo) {
        this.userPartRepository = userPartRepository;
        this.userPartMapper = userPartMapper;
        this.sparePartRepo = sparePartRepo;
    }

    @Override
    public UserPartDto getUserPartById(Integer userPartId) {
        return userPartRepository.findById(userPartId)
                .map(UserPartDto::new)
                .orElseThrow(() -> new RuntimeException("UserPart not found with id " + userPartId));
    }

    @Override
    public PaginatedResponse<UserPartDto> getAllUserParts(int page, int size) {
        Sort sort = Sort.by("userPartId").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserPartProjection> userPartPage = userPartRepository.findAllProjected(pageable);

        if (userPartPage.isEmpty()) {
            throw new RuntimeException("No data found");
        }

        List<UserPartDto> userPartDtoList = userPartPage
                .map(projection -> UserPartDto.builder()
                        .userPartId(projection.getUserPartId())
                        .partNumber(projection.getPartNumber())
                        .partName(projection.getPartName())
                        .manufacturer(projection.getManufacturer())
                        .quantity(projection.getQuantity())
                        .price(projection.getPrice())
                        .buyingPrice(projection.getBuyingPrice())
                        .updateAt(projection.getUpdateAt())
                        .description(projection.getDescription())
                        .vendor(projection.getVendor())
                        .build())
                .getContent();

        return new PaginatedResponse<>(
                userPartDtoList,
                userPartPage.getTotalPages(),
                userPartPage.getTotalElements(),
                page
        );
    }
    @Override
    public Integer getQuantityByPartNumber(String partNumber) {
        return userPartRepository.findQuantityByPartNumber(partNumber)
                .orElseThrow(() -> new IllegalArgumentException("No stock entry found for Part Number: " + partNumber));
    }

    @Override
    public UserPartDto updateUserPartAndSparePart(UpdateUserPartRequestDTO request) {
        UserPart userPart = userPartRepository.findById(request.getUserPartId())
                .orElseThrow(() -> new RuntimeException("UserPart not found"));
        Optional.ofNullable(request.getPartName()).ifPresent(userPart::setPartName);
        Optional.ofNullable(request.getDescription()).ifPresent(userPart::setDescription);
        Optional.ofNullable(request.getManufacturer()).ifPresent(userPart::setManufacturer);
        Optional.ofNullable(request.getPrice()).ifPresent(userPart::setPrice);
        Optional.ofNullable(request.getPartNumber()).ifPresent(userPart::setPartNumber);
        Optional.ofNullable(request.getCGST()).ifPresent(userPart::setCGST);
        Optional.ofNullable(request.getSGST()).ifPresent(userPart::setSGST);
        Optional.ofNullable(request.getTotalGST()).ifPresent(userPart::setTotalGST);
        Optional.ofNullable(request.getBuyingPrice()).ifPresent(userPart::setBuyingPrice);
        Optional.ofNullable(request.getQuantity()).ifPresent(userPart::setQuantity);

        SparePart sparePart = sparePartRepo.findById(userPart.getSparePart().getSparePartId())
                .orElseThrow(() -> new RuntimeException("SparePart not found"));

        Optional.ofNullable(request.getPartName()).ifPresent(sparePart::setPartName);
        Optional.ofNullable(request.getDescription()).ifPresent(sparePart::setDescription);
        Optional.ofNullable(request.getManufacturer()).ifPresent(sparePart::setManufacturer);
        Optional.ofNullable(request.getPrice()).ifPresent(sparePart::setPrice);
        Optional.ofNullable(request.getPartNumber()).ifPresent(sparePart::setPartNumber);
        Optional.ofNullable(request.getCGST()).ifPresent(sparePart::setCGST);
        Optional.ofNullable(request.getSGST()).ifPresent(sparePart::setSGST);
        Optional.ofNullable(request.getTotalGST()).ifPresent(sparePart::setTotalGST);
        Optional.ofNullable(request.getBuyingPrice()).ifPresent(sparePart::setBuyingPrice);

        SparePart updatedSparePart = sparePartRepo.save(sparePart);

        userPart.setSparePart(updatedSparePart);

        UserPart updatedUserPart = userPartRepository.save(userPart);

        return userPartMapper.toDto(updatedUserPart);
    }

    @Override
    @Transactional
    public void deleteUserPartAndSparePart(Integer userPartId) {
        UserPart userPart = userPartRepository.findById(userPartId)
                .orElseThrow(() -> new RuntimeException("UserPart not found with id " + userPartId));
        
        Integer sparePartId = userPart.getSparePart().getSparePartId();
        
        // Delete UserPart first (child)
        userPartRepository.delete(userPart);
        
        // Then delete SparePart (parent)
        sparePartRepo.deleteById(sparePartId);
    }
}
