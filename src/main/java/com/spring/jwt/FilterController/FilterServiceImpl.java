package com.spring.jwt.FilterController;

import com.spring.jwt.SparePart.*;
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

    public final SparePartMapper sparePartMapper;

    @Override
    public List<SparePartDto> searchBarFilter(String searchBarInput) {

        String[] tokens = searchBarInput.toLowerCase().trim().split("\\s+");

        List<SparePart> spareParts = filterRepository.findAll((root, query, cb) -> {
            root.fetch("photo", JoinType.LEFT);
            query.distinct(true);

            List<Predicate> tokenPredicates = new ArrayList<>();

            for (String token : tokens) {
                String pattern = "%" + token + "%";

                Predicate orForThisToken = cb.or(
                        cb.like(cb.lower(root.get("partName")), pattern),
                        cb.like(cb.lower(root.get("description")), pattern),
                        cb.like(cb.lower(root.get("manufacturer")), pattern),
                        cb.like(cb.function("concat", String.class, cb.literal(""), root.get("partNumber")) , pattern)
                );

                tokenPredicates.add(orForThisToken);
            }

            return cb.and(tokenPredicates.toArray(new Predicate[0]));
        });

        if (spareParts.isEmpty()) {
            throw new PageNotFoundException("No spare parts found for the given search keyword.");
        }
        return spareParts.stream()
                .map(sparePartMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SpareFilterDto> searchSpareParts(String keyword, int limit) {
        long startTime = System.currentTimeMillis();
        logger.info("Starting search for keyword: {}", keyword);

        try {
            // First try to use the Hibernate API which is safer
            // This avoids SQL syntax issues with column names
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

            // Log the actual entity data to diagnose the issue
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
                        // Create DTO with proper price value
                        SpareFilterDto dto = new SpareFilterDto();
                        dto.setSparePartId(sparePart.getSparePartId());
                        dto.setPartName(sparePart.getPartName());
                        dto.setDescription(sparePart.getDescription());
                        dto.setManufacturer(sparePart.getManufacturer());

                        // Make sure price is properly set - handle nulls and use direct access
                        Long priceValue = sparePart.getPrice();
                        logger.debug("Raw price value for part {}: {}", sparePart.getPartName(), priceValue);

                        if (priceValue == null) {
                            // Default to zero if null
                            dto.setPrice(0L);
                        } else {
                            // Ensure the Long value is correctly set
                            dto.setPrice(priceValue);
                        }

                        dto.setPartNumber(sparePart.getPartNumber());
                        dto.setCGST(sparePart.getCGST());
                        dto.setSGST(sparePart.getSGST());
                        dto.setTotalGST(sparePart.getTotalGST());
                        dto.setBuyingPrice(sparePart.getBuyingPrice());

                        // Log the values to help diagnose issues
                        logger.debug("Mapped price: {} for part {}", dto.getPrice(), dto.getPartName());

                        return dto;
                    })
                    .collect(Collectors.toList());

            // Log the first DTO to confirm the price is set correctly
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
            // Return empty list rather than throwing exception
            return new ArrayList<>();
        }
    }
}
