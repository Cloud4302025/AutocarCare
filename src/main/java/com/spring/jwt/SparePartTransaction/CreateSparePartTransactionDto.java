package com.spring.jwt.SparePartTransaction;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSparePartTransactionDto {
    private String partNumber;
    private TransactionType transactionType;
    private String manufacturer;
    private Integer quantity;
    private Integer userId;
    private String billNo;
    private Integer vehicleRegId;
    private Integer cGST;
    private Integer sGST;
    private String customerName;
    private String name;
    private Integer vendorId;
}
