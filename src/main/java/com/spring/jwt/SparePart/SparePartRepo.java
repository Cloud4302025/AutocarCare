package com.spring.jwt.SparePart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SparePartRepo extends JpaRepository<SparePart, Integer>, JpaSpecificationExecutor<SparePart> {
    Optional<Object> findByPartNumber(String partNumber);

    @EntityGraph(attributePaths = {"photo"})
    Page<SparePart> findAllByOrderBySparePartIdDesc(Pageable pageable);

    @Query("SELECT s FROM SparePart s " +
            "WHERE LOWER(s.partName) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR LOWER(s.description) LIKE CONCAT('%', LOWER(:keyword), '%') " +
            "OR CAST(s.partNumber AS string) LIKE CONCAT('%', :keyword, '%') " +
            "OR LOWER(s.manufacturer) LIKE CONCAT('%', LOWER(:keyword), '%')")
    List<SparePart> searchSparePartsByKeyword(@Param("keyword") String keyword);

    Optional<SparePart> findByPartNumberAndManufacturer(String partNumber, String manufacturer);

    @Query("SELECT COUNT(s) > 0 FROM SparePart s WHERE s.partNumber = :partNumber")
    boolean existsByPartNumber(@Param("partNumber") String partNumber);

    @Query("SELECT MAX(s.sparePartId) FROM SparePart s")
    Integer findMaxId();

    @Query("SELECT s.sparePartId as sparePartId, " +
           "s.partName as partName, " +
           "s.manufacturer as manufacturer, " +
           "s.price as price, " +
           "s.partNumber as partNumber " +
           "FROM SparePart s " +
           "ORDER BY s.sparePartId DESC")
    Page<SparePartProjection> findAllProjectedBy(Pageable pageable);
    
    @Query("SELECT p FROM SparePart s JOIN s.photo p WHERE s.sparePartId = :id ORDER BY p LIMIT 1")
    Optional<byte[]> findFirstPhotoById(@Param("id") Integer id);

    @Query("SELECT DISTINCT s.manufacturer FROM SparePart s WHERE s.manufacturer IS NOT NULL")
    List<String> findDistinctManufacturers();

}

