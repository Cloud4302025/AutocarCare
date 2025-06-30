package com.spring.jwt.SparePartTransaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartBasicInfoDto {
    private String partName;
    private String manufacturer;
    private String partNumber;
} 