package com.spring.jwt.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeePaymentsDTO {

    private Integer id;
    private LocalDate joiningDate;
    private String name;
    private Integer salary;
    private Integer advancePayment;

}
