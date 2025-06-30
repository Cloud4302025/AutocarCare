package com.spring.jwt.SparePartTransaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemWiseTransactionDto {
    private Integer sparePartId;
    private String partNumber;
    private String partName;
    private String manufacturer;
    private Long price;
    private Integer cGST;
    private Integer sGST;
    private Long qtyPrice;
    private LocalDateTime updateAt;
    private Integer saleQuantity; // Total transacted quantity
    private Integer remainingQuantity; // Current quantity from UserPart
} 