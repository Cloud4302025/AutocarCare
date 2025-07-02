package com.spring.jwt.SparePartTransaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SparePartTransactionRepository extends JpaRepository<SparePartTransaction, Integer> {

    List<SparePartTransaction> findByVehicleRegId(Integer vehicleRegID);

    List<SparePartTransaction> findByUserId(Integer userId);

    List<SparePartTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<SparePartTransaction> findByBillNo(String billNo);

    List<SparePartTransaction> findBySparePartIdAndTransactionDateBetween(Integer sparePartId, LocalDateTime startDate, LocalDateTime endDate);

    List<SparePartTransaction> findByTransactionTypeAndNameAndTransactionDateBetween(TransactionType transactionType, String name, LocalDateTime startDate, LocalDateTime endDate);

    List<SparePartTransaction> findByNameOrPartNumber(String name, String partNumber);

    List<SparePartTransaction> findByName(String name);

    List<SparePartTransaction> findByPartNumber(String partNumber);


    @Query("SELECT t FROM SparePartTransaction t " +
            "WHERE t.transactionType = :transactionType " +
            "AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<SparePartTransaction> findByTransactionTypeAndTransactionDateBetween(
            @Param("transactionType") TransactionType transactionType,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);


    List<SparePartTransaction> findByTransactionDateBetweenOrderByNameAsc(
            LocalDateTime startDate, LocalDateTime endDate);

    List<SparePartTransaction> findByTransactionDateBetweenOrderByPartNameAsc(
            LocalDateTime startDate, LocalDateTime endDate);

    /**
     * New: fetch only the distinct vendor names in the date range, sorted.
     */
    @Query("""
        SELECT DISTINCT t.name
        FROM SparePartTransaction t
        WHERE t.transactionDate BETWEEN :start AND :end
        ORDER BY t.name
        """)
    List<String> findDistinctNamesByTransactionDateBetweenOrderByNameAsc(
            @Param("start") LocalDateTime start,
            @Param("end"  ) LocalDateTime end
    );
    
    /**
     * Optimized query that fetches aggregated transaction data with sale quantities
     * directly from the database, along with remaining quantities from user_part table.
     * This avoids N+1 query problems and reduces data processing in Java.
     */
    @Query(value = """
        SELECT 
            t.spare_part_id AS sparePartId,
            t.part_number AS partNumber, 
            t.part_name AS partName,
            t.manufacturer AS manufacturer,
            t.price AS price,
            t.cgst AS cGST,
            t.sgst AS sGST,
            t.qty_price AS qtyPrice,
            SUM(t.quantity) AS saleQuantity,
            COALESCE(u.quantity, 0) AS remainingQuantity
        FROM 
            (SELECT 
                spt.spare_part_id,
                spt.part_number,
                spt.part_name, 
                spt.manufacturer,
                spt.price,
                spt.cgst,
                spt.sgst,
                spt.qty_price,
                spt.quantity 
             FROM 
                spare_part_transaction spt
             WHERE 
                spt.transaction_date BETWEEN :fromDate AND :toDate
                AND spt.spare_part_id IS NOT NULL) t
        LEFT JOIN 
            user_part u ON u.spare_part_id = t.spare_part_id
        GROUP BY 
            t.spare_part_id, 
            t.part_number,
            t.part_name,
            t.manufacturer,
            t.price,
            t.cgst,
            t.sgst,
            t.qty_price,
            u.quantity
        ORDER BY 
            t.part_name ASC
        """, nativeQuery = true)
    List<Object[]> findAggregatedTransactionsByDateRange(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate);

    /**
     * Optimized query that fetches aggregated transaction data for a specific vendor
     * with sale quantities directly from the database, along with remaining quantities.
     */
    @Query(value = """
        SELECT 
            t.spare_part_id AS sparePartId,
            t.part_number AS partNumber, 
            t.part_name AS partName,
            t.manufacturer AS manufacturer,
            t.price AS price,
            t.cgst AS cGST,
            t.sgst AS sGST,
            t.qty_price AS qtyPrice,
            SUM(t.quantity) AS saleQuantity,
            COALESCE(u.quantity, 0) AS remainingQuantity
        FROM 
            (SELECT 
                spt.spare_part_id,
                spt.part_number,
                spt.part_name, 
                spt.manufacturer,
                spt.price,
                spt.cgst,
                spt.sgst,
                spt.qty_price,
                spt.quantity 
             FROM 
                spare_part_transaction spt
             WHERE 
                spt.name = :vendorName
                AND spt.spare_part_id IS NOT NULL) t
        LEFT JOIN 
            user_part u ON u.spare_part_id = t.spare_part_id
        GROUP BY 
            t.spare_part_id, 
            t.part_number,
            t.part_name,
            t.manufacturer,
            t.price,
            t.cgst,
            t.sgst,
            t.qty_price,
            u.quantity
        ORDER BY 
            t.part_name ASC
        """, nativeQuery = true)
    List<Object[]> findAggregatedTransactionsByVendorName(
            @Param("vendorName") String vendorName);
}


