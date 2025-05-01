package com.spring.jwt.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorPartDto {

    private Integer vendorPartId;

    private Integer sparePartId;

    private String partName;

    private String description;

    private String manufacturer;

    private Long price;

    private LocalDate updateAt;

    private String partNumber;

    private Integer vendorId;

    private Integer cGST;

    private Integer sGST;

    private Integer totalGST;

    private Integer buyingPrice;

    private String vendor;
}
