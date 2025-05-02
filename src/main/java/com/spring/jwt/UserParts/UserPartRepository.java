package com.spring.jwt.UserParts;

import com.spring.jwt.SparePart.SparePart;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserPartRepository extends JpaRepository<UserPart, Integer>, JpaSpecificationExecutor<UserPart> {
    Optional<UserPart> findBySparePart_SparePartId(Integer sparePartId);

    @Lock(LockModeType.PESSIMISTIC_WRITE) // Lock to prevent concurrent updates
    @Query("SELECT u FROM UserPart u WHERE u.partNumber = :partNumber")
    Optional<UserPart> findByPartNumber(@Param("partNumber") String partNumber);

    @Modifying
    @Query("UPDATE UserPart u SET u.quantity = u.quantity WHERE u.partNumber = :partNumber")
    void refreshUserPart(@Param("partNumber") String partNumber);


    void deleteBySparePart(SparePart sparePart);

    @Query("select u.userPartId as userPartId, u.partNumber as partNumber, u.partName as partName, " +
            "u.manufacturer as manufacturer,u.buyingPrice as buyingPrice, u.quantity as quantity, u.price as price,u.description as description, u.updateAt as updateAt " +
            "from UserPart u")
    Page<UserPartProjection> findAllProjected(Pageable pageable);

    Page<UserPart> findAll(Pageable pageable);

    @Query("SELECT u.quantity FROM UserPart u WHERE u.partNumber = :partNumber")
    Optional<Integer> findQuantityByPartNumber(@Param("partNumber") String partNumber);

    Optional<UserPart> findByPartNumberAndManufacturer(String partNumber, String manufacturer);
}
