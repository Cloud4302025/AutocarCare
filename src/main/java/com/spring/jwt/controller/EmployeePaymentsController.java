package com.spring.jwt.controller;


import com.spring.jwt.dto.EmployeePaymentsDTO;
import com.spring.jwt.dto.UpdateEmployeePaymentRequest;
import com.spring.jwt.service.EmployeePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeePaymentsController {

    @Autowired
    private EmployeePaymentsService service;

    @PostMapping
    public EmployeePaymentsDTO createEmployee(@RequestBody EmployeePaymentsDTO dto) {
        return service.createEmployee(dto);
    }

    @GetMapping("/{id}")
    public EmployeePaymentsDTO getEmployeeById(@PathVariable Integer id) {
        return service.getEmployeeById(id);
    }

    @GetMapping
    public List<EmployeePaymentsDTO> getAllEmployees() {
        return service.getAllEmployees();
    }

    @PatchMapping("/{id}")
    public EmployeePaymentsDTO updateEmployeePayments(@PathVariable Integer id,
                                                      @RequestBody UpdateEmployeePaymentRequest request) {
        return service.updateEmployeePayments(id, request.getSalary(), request.getAdvancePayment());
    }
}

