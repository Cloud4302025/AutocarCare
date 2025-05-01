package com.spring.jwt.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BankDepositDTO {

    private Long id;
    private LocalDate depositDate;
    private Integer amount;
}
