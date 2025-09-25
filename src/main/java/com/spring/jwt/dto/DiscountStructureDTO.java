package com.spring.jwt.dto;

import lombok.Data;

@Data
public class DiscountStructureDTO {
    private Integer discountId;
    private String manufacturer;
    private Integer discountA;
    private Integer discountB;
    private Integer discountC;
    private Integer activeSetIndex;

    private Integer discount;
}
