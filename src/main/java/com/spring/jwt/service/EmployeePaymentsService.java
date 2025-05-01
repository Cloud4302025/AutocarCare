package com.spring.jwt.service;

import com.spring.jwt.dto.EmployeePaymentsDTO;

import java.util.List;

public interface EmployeePaymentsService {
    EmployeePaymentsDTO createEmployee(EmployeePaymentsDTO dto);
    EmployeePaymentsDTO getEmployeeById(Integer id);
    List<EmployeePaymentsDTO> getAllEmployees();
    EmployeePaymentsDTO updateEmployeePayments(Integer id, Integer salary, Integer advancePayment);
}