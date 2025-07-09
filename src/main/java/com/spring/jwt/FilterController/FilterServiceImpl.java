package com.spring.jwt.FilterController;

import com.spring.jwt.SparePart.*;
import com.spring.jwt.UserParts.UserPart;
import com.spring.jwt.UserParts.UserPartDto;
import com.spring.jwt.UserParts.UserPartRepository;
import com.spring.jwt.exception.PageNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService {

    private static final Logger logger = LoggerFactory.getLogger(FilterServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    public final SparePartRepo filterRepository;

    public final UserPartRepository userPartRepository;

    public final SparePartMapper sparePartMapper;

    @Override
    @Cacheable(value = "searchBarFilterCache", key = "#searchBarInput.toLowerCase()")
    public List<SparePartDto> searchBarFilter(String searchBarInput) {
        String[] tokens = searchBarInput.toLowerCase().trim().split("\\s+");
        Pageable pageable = PageRequest.of(0, 20);

        Specification<SparePart> spec = (root, query, cb) -> {
            List<Predicate> tokenPredicates = new ArrayList<>();
            for (String token : tokens) {
                String pattern = "%" + token + "%";
                Predicate orForThisToken = cb.or(
                        cb.like(cb.lower(root.get("partName")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern),
                        cb.like(cb.lower(root.get("manufacturer")), pattern),
                        cb.like(cb.function("concat", String.class, cb.literal(""), root.get("partNumber")), pattern)
                );
                tokenPredicates.add(orForThisToken);
            }
            return cb.and(tokenPredicates.toArray(new Predicate[0]));
        };

        Page<SparePart> spareParts = filterRepository.findAll(spec, pageable);
        if (spareParts.isEmpty()) {
            throw new PageNotFoundException("No spare parts found for the given search keyword.");
        }
        return spareParts.getContent().stream()
                .map(sparePartMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpareFilterDto> searchSpareParts(String keyword, int limit) {
        long startTime = System.currentTimeMillis();
        logger.info("Starting search for keyword: {}", keyword);

        try {
            Specification<SparePart> spec = (root, query, cb) -> {
                List<Predicate> tokenPredicates = new ArrayList<>();
                String[] tokens = keyword.trim().toLowerCase().split("\\s+");

                for (String token : tokens) {
                    String pattern = "%" + token + "%";
                    Predicate tokenPredicate = cb.or(
                            cb.like(cb.lower(root.get("partName")), pattern),
                            cb.like(cb.lower(root.get("description")), pattern),
                            cb.like(cb.lower(root.get("manufacturer")), pattern),
                            cb.like(cb.lower(root.get("partNumber")), pattern)
                    );
                    tokenPredicates.add(tokenPredicate);
                }
                return cb.and(tokenPredicates.toArray(new Predicate[0]));
            };

            Pageable pageable = PageRequest.of(0, limit);
            Page<SparePart> pageResult = filterRepository.findAll(spec, pageable);

            if (!pageResult.isEmpty()) {
                SparePart firstPart = pageResult.getContent().get(0);
                logger.info("First part found: id={}, name={}, price={}, class={}",
                        firstPart.getSparePartId(),
                        firstPart.getPartName(),
                        firstPart.getPrice(),
                        firstPart.getPrice() != null ? firstPart.getPrice().getClass().getName() : "null");
            }

            List<SpareFilterDto> dtoList = pageResult.getContent().stream()
                    .map(sparePart -> {
                        SpareFilterDto dto = new SpareFilterDto();
                        dto.setSparePartId(sparePart.getSparePartId());
                        dto.setPartName(sparePart.getPartName());
                        dto.setDescription(sparePart.getDescription());
                        dto.setManufacturer(sparePart.getManufacturer());

                        Long priceValue = sparePart.getPrice();
                        logger.debug("Raw price value for part {}: {}", sparePart.getPartName(), priceValue);

                        if (priceValue == null) {
                            dto.setPrice(0L);
                        } else {
                            dto.setPrice(priceValue);
                        }

                        dto.setPartNumber(sparePart.getPartNumber());
                        dto.setCGST(sparePart.getCGST());
                        dto.setSGST(sparePart.getSGST());
                        dto.setTotalGST(sparePart.getTotalGST());
                        dto.setBuyingPrice(sparePart.getBuyingPrice());

                        logger.debug("Mapped price: {} for part {}", dto.getPrice(), dto.getPartName());

                        return dto;
                    })
                    .collect(Collectors.toList());

            if (!dtoList.isEmpty()) {
                SpareFilterDto firstDto = dtoList.get(0);
                logger.info("First DTO: id={}, name={}, price={}",
                        firstDto.getSparePartId(),
                        firstDto.getPartName(),
                        firstDto.getPrice());
            }

            long endTime = System.currentTimeMillis();
            logger.info("Search completed in {} ms, found {} results", (endTime - startTime), dtoList.size());

            return dtoList;
        } catch (Exception e) {
            logger.error("Error in search: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getAllManufacturers() {
        return filterRepository.findDistinctManufacturers();
    }

    @Override
    @Cacheable(value = "searchBarFilterCache", key = "#searchBarInput.toLowerCase()")
    public List<UserPartDto> searchBarFilterUserPart(String searchBarInput) {
        String[] tokens = searchBarInput.toLowerCase().trim().split("\\s+");
        Pageable pageable = PageRequest.of(0, 20);

        Specification<UserPart> spec = (root, query, cb) -> {
            List<Predicate> tokenPredicates = new ArrayList<>();
            for (String token : tokens) {
                String pattern = "%" + token + "%";
                Predicate orForThisToken = cb.or(
                        cb.like(cb.lower(root.get("partName")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern),
                        cb.like(cb.lower(root.get("manufacturer")), pattern),
                        cb.like(cb.lower(root.get("partNumber")), pattern),
                        cb.like(cb.function("str", String.class, root.get("quantity")), pattern) // include quantity
                );
                tokenPredicates.add(orForThisToken);
            }
            return cb.and(tokenPredicates.toArray(new Predicate[0]));
        };

        Page<UserPart> spareParts = userPartRepository.findAll(spec, pageable);
        if (spareParts.isEmpty()) {
            throw new PageNotFoundException("No spare parts found for the given search keyword.");
        }

        return spareParts.getContent().stream()
                .map(this::toUserPartDto)
                .collect(Collectors.toList());

    }

    public UserPartDto toUserPartDto(UserPart userPart) {
        return UserPartDto.builder()
                .userPartId(userPart.getUserPartId())
                .quantity(userPart.getQuantity())
                .lastUpdate(userPart.getLastUpdate())
                .partName(userPart.getPartName())
                .description(userPart.getDescription())
                .manufacturer(userPart.getManufacturer())
                .price(userPart.getPrice())
                .updateAt(userPart.getUpdateAt())
                .partNumber(userPart.getPartNumber())
                .vendor(userPart.getVendor())
                .cGST(userPart.getCGST())
                .sGST(userPart.getSGST())
                .totalGST(userPart.getTotalGST())
                .buyingPrice(userPart.getBuyingPrice())
                .sparePartId(userPart.getSparePart() != null ? userPart.getSparePart().getSparePartId() : null)
                .build();
    }


}






