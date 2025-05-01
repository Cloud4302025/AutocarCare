package com.spring.jwt.vender;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ResponseAllVendorDto {
    private List<VendorDto> vendors;
    private String message;
}

