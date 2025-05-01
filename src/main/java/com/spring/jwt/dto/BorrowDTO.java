package com.spring.jwt.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BorrowDTO {

    private Integer borrowId;
    private String customerName;
    private LocalDate remainingDate;
    private Long remainingPayment;
}
