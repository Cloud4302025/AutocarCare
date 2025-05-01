package com.spring.jwt.SparePart;

import com.spring.jwt.UserParts.UserPart;
import com.spring.jwt.UserParts.UserPartRepository;
import com.spring.jwt.VehicleReg.BadRequestException;
import com.spring.jwt.exception.SparePartNotFoundException;
import com.spring.jwt.utils.BaseResponseDTO;
import com.spring.jwt.utils.ImageCompressionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SparePartServiceImpl implements SparePartService {

    public final SparePartRepo sparePartRepo;

    public final SparePartMapper sparePartMapper;

    public final UserPartRepository userPartRepo;

    public static final Logger logger = LoggerFactory.getLogger(SparePartServiceImpl.class);

    @Override
    public BaseResponseDTO addPart(String partName, String description, String manufacturer, Long price, String partNumber, List<MultipartFile> photos,Integer sGST,Integer cGST,Integer totalGST,Integer buyingPrice) {
        // Clean input data
        String cleanPartNumber = partNumber.trim();
        String cleanManufacturer = manufacturer.trim();

        // Log what we're checking to help diagnose the issue
        logger.info("Checking for duplicate part. Part Number: '{}', Manufacturer: '{}'", cleanPartNumber, cleanManufacturer);

        // First check if the part number exists regardless of manufacturer
        if (sparePartRepo.existsByPartNumber(cleanPartNumber)) {
            logger.warn("Part with part number {} already exists in the database", cleanPartNumber);
            throw new BadRequestException("Part number " + cleanPartNumber + " already exists.");
        }

        // Then check with manufacturer too
        Optional<SparePart> existingPart = sparePartRepo.findByPartNumberAndManufacturer(cleanPartNumber, cleanManufacturer);

        if (existingPart.isPresent()) {
            logger.warn("Part with part number {} already exists for manufacturer {}", cleanPartNumber, cleanManufacturer);
            throw new BadRequestException("Part with part number " + cleanPartNumber + " already exists for manufacturer " + cleanManufacturer);
        }

        // Final case insensitive check to be extra safe
        List<SparePart> allParts = sparePartRepo.findAll();
        boolean duplicateFound = false;

        for (SparePart part : allParts) {
            if (part.getPartNumber() != null && part.getPartNumber().equalsIgnoreCase(cleanPartNumber)) {
                duplicateFound = true;
                logger.warn("Found case-insensitive duplicate for part number: {} vs {}",
                        cleanPartNumber, part.getPartNumber());
                break;
            }
        }

        if (duplicateFound) {
            logger.warn("Part with part number {} already exists (case-insensitive match)", cleanPartNumber);
            throw new BadRequestException("Part with part number " + cleanPartNumber + " already exists (ignoring case)");
        }

        try {
            // Initialize empty list to avoid nulls
            List<byte[]> compressedPhotos = new ArrayList<>();

            // Process photos only if available
            if (photos != null && !photos.isEmpty()) {
                for (MultipartFile file : photos) {
                    try {
                        if (file == null || file.isEmpty() || file.getBytes().length == 0) {
                            logger.warn("Empty image file detected, skipping");
                            continue;
                        }

                        // Log image details for debugging
                        logger.info("Processing image: name={}, size={}, contentType={}",
                                file.getOriginalFilename(),
                                file.getSize(),
                                file.getContentType());

                        byte[] compressed = ImageCompressionUtil.compressImage(file.getBytes());

                        if (compressed != null && compressed.length > 0) {
                            compressedPhotos.add(compressed);
                            logger.info("Successfully compressed image {} from {} bytes to {} bytes",
                                    file.getOriginalFilename(),
                                    file.getSize(),
                                    compressed.length);
                        } else {
                            logger.warn("Image compression returned null or empty result for {}", file.getOriginalFilename());
                        }
                    } catch (IOException e) {
                        logger.error("IO Exception processing image {}: {}", file.getOriginalFilename(), e.getMessage(), e);
                        // Continue with other images instead of failing completely
                    } catch (Exception e) {
                        logger.error("Unexpected error processing image {}: {}", file.getOriginalFilename(), e.getMessage(), e);
                        // Continue with other images instead of failing completely
                    }
                }
            }

            // Even if some images failed, proceed with the images that worked
            logger.info("Processed {} images successfully out of {} total",
                    photos != null ? compressedPhotos.size() : 0,
                    photos != null ? photos.size() : 0);

            // Check current max ID in database and adjust if needed
            Integer maxId = sparePartRepo.findMaxId();
            if (maxId != null) {
                logger.info("Current maximum ID in database: {}", maxId);
            }

            // Using the builder to create the entity
            SparePart sparePart = SparePart.builder()
                    .partName(partName)
                    .description(description)
                    .manufacturer(cleanManufacturer)
                    .price(price)
                    .partNumber(cleanPartNumber)
                    .updateAt(LocalDate.now())
                    .sGST(sGST)
                    .cGST(cGST)
                    .buyingPrice(buyingPrice)
                    .totalGST(totalGST)
                    .photo(new ArrayList<>()) // Initialize with empty list first
                    .build();

            // Save the part first to get the ID generated by the sequence
            sparePart = sparePartRepo.save(sparePart);

            logger.info("Saved new spare part with ID: {}", sparePart.getSparePartId());

            // After saving, add the photos to the saved entity
            if (!compressedPhotos.isEmpty()) {
                sparePart.getPhoto().addAll(compressedPhotos);
                // Save again to persist the photos with the correct spare_part_id
                sparePart = sparePartRepo.save(sparePart);
                logger.info("Saved {} photos for spare part ID: {}", compressedPhotos.size(), sparePart.getSparePartId());
            }

            // Now create user part referencing the saved part with its ID
            UserPart userPart = UserPart.builder()
                    .partName(partName)
                    .description(description)
                    .manufacturer(cleanManufacturer)
                    .price(price)
                    .partNumber(cleanPartNumber)
                    .updateAt(LocalDate.now())
                    .quantity(0)
                    .sparePart(sparePart)  // Reference the saved part with ID
                    .lastUpdate(LocalDate.now().toString())
                    .sGST(sGST)
                    .cGST(cGST)
                    .buyingPrice(buyingPrice)
                    .totalGST(totalGST)
                    .build();

            userPartRepo.save(userPart);
            logger.info("Saved new user part for spare part ID: {}", sparePart.getSparePartId());

            return new BaseResponseDTO("Success", "Part Added Successfully");

        } catch (DataIntegrityViolationException e) {
            logger.error("Database constraint violation: {}", e.getMessage(), e);
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry") && e.getMessage().contains("PRIMARY")) {
                throw new BadRequestException("ID already exists in database. Please try again.");
            } else if (e.getMessage() != null && e.getMessage().contains("spare_part_id")) {
                throw new BadRequestException("Failed to save part photos. Database error with spare_part_id.");
            } else if (e.getMessage() != null && e.getMessage().contains("part_number")) {
                throw new BadRequestException("Part number " + cleanPartNumber + " already exists.");
            } else {
                throw new BadRequestException("Database error: " + e.getMessage());
            }
        } catch (RuntimeException e) {
            logger.error("Error processing request: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to add part: " + e.getMessage());
        }
    }

    @Override
    public SparePartDto getSparePartById(Integer id) {
        Optional<SparePart> sparePartOptional = sparePartRepo.findById(id);

        return sparePartOptional.map(sparePart -> SparePartDto.builder()
                .sparePartId(sparePart.getSparePartId())
                .partName(sparePart.getPartName())
                .description(sparePart.getDescription())
                .manufacturer(sparePart.getManufacturer())
                .price(sparePart.getPrice())
                .partNumber(sparePart.getPartNumber())
                .updateAt(sparePart.getUpdateAt())
                .photo(sparePart.getPhoto().stream()
                        .map(photo -> Base64.getEncoder().encodeToString(photo))
                        .toList())
                .buyingPrice(sparePart.getBuyingPrice())
                .cGST(sparePart.getCGST())
                .sGST(sparePart.getSGST())
                .buyingPrice(sparePart.getBuyingPrice())
                .vendor(sparePart.getVendor())
                .build()
        ).orElse(null);
    }

    @Override
    public PaginatedResponse<SparePartDto> getAllSpareParts(int page, int size) {
        Sort sort = Sort.by("sparePartId").descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SparePartProjection> sparePartsPage = sparePartRepo.findAllProjectedBy(pageable);

        if (sparePartsPage.isEmpty()) {
            throw new RuntimeException("No data found");
        }

        List<SparePartDto> sparePartsDtoList = sparePartsPage
                .map(projection -> {
                    List<String> base64Photos = null;
                    if (projection.getPhoto() != null) {
                        base64Photos = projection.getPhoto().stream()
                                .map(bytes -> Base64.getEncoder().encodeToString(bytes))
                                .collect(Collectors.toList());
                    }
                    return SparePartDto.builder()
                            .sparePartId(projection.getSparePartId())
                            .partName(projection.getPartName())
                            .manufacturer(projection.getManufacturer())
                            .price(projection.getPrice())
                            .partNumber(projection.getPartNumber())
                            .photo(base64Photos)
                            .build();
                })
                .getContent();

        return new PaginatedResponse<>(
                sparePartsDtoList,
                sparePartsPage.getTotalPages(),
                sparePartsPage.getTotalElements(),
                page
        );
    }


    @Override
    public SparePartDto updatePart(Integer id, String partName, String description, String manufacturer, Long price, String partNumber, List<MultipartFile> photos,Integer sGST,Integer cGST,Integer totalGST,Integer buyingPrice,String vendor) {
        SparePart sparePart = sparePartRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Spare part not found"));

        Optional.ofNullable(partName).ifPresent(sparePart::setPartName);
        Optional.ofNullable(description).ifPresent(sparePart::setDescription);
        Optional.ofNullable(manufacturer).ifPresent(sparePart::setManufacturer);
        Optional.ofNullable(price).ifPresent(sparePart::setPrice);
        Optional.ofNullable(partNumber).ifPresent(sparePart::setPartNumber);
        Optional.ofNullable(cGST).ifPresent(sparePart::setCGST);
        Optional.ofNullable(sGST).ifPresent(sparePart::setSGST);
        Optional.ofNullable(totalGST).ifPresent(sparePart::setTotalGST);
        Optional.ofNullable(buyingPrice).ifPresent(sparePart::setBuyingPrice);
        Optional.ofNullable(vendor).ifPresent(sparePart::setVendor);

        if (photos != null && !photos.isEmpty()) {
            try {
                // Compress new photos
                List<byte[]> compressedPhotos = photos.stream()
                        .map(file -> {
                            try {
                                byte[] compressed = ImageCompressionUtil.compressImage(file.getBytes());
                                if (compressed == null || compressed.length == 0) {
                                    logger.error("Compressed image is empty for file: {}", file.getOriginalFilename());
                                    throw new RuntimeException("Compressed image is empty");
                                }
                                return compressed;
                            } catch (IOException e) {
                                logger.error("Failed to compress image: {}", file.getOriginalFilename(), e);
                                throw new RuntimeException("Failed to compress image", e);
                            }
                        })
                        .collect(Collectors.toList());

                logger.info("Number of compressed photos: {}", compressedPhotos.size());


                List<byte[]> existingPhotos = sparePart.getPhoto();
                if (existingPhotos == null) {
                    existingPhotos = new ArrayList<>();
                }
                existingPhotos.addAll(compressedPhotos);
                sparePart.setPhoto(existingPhotos);
            } catch (Exception e) {
                logger.error("Failed to upload images", e);
                throw new RuntimeException("Failed to upload images", e);
            }
        } else {
            logger.warn("No photos provided for update. Skipping photo update.");
        }

        SparePart updatedPart = sparePartRepo.save(sparePart);

        logger.info("Updated spare part: {}", updatedPart);

        return sparePartMapper.toDto(updatedPart);
    }
    @Transactional
    @Override
    public BaseResponseDTO deleteSparePartById(Integer id, Integer photoIndex) {
        return sparePartRepo.findById(id)
                .map(sparePart -> {

                    userPartRepo.deleteBySparePart(sparePart);

                    if (photoIndex != null) {
                        List<byte[]> photos = sparePart.getPhoto();
                        if (photoIndex >= 0 && photoIndex < photos.size()) {
                            photos.remove(photoIndex.intValue());
                            sparePart.setPhoto(photos);
                            sparePartRepo.save(sparePart);
                            return BaseResponseDTO.builder()
                                    .code("SUCCESS")
                                    .message("Photo deleted successfully")
                                    .build();
                        } else {
                            throw new IllegalArgumentException("Invalid photo index: " + photoIndex);
                        }
                    } else {
                        sparePartRepo.deleteById(id);
                        return BaseResponseDTO.builder()
                                .code("SUCCESS")
                                .message("Spare part deleted successfully")
                                .build();
                    }
                })
                .orElseThrow(() -> new SparePartNotFoundException("Not found with ID: " + id));
    }

}
