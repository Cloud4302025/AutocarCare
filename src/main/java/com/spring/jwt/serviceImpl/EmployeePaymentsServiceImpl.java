package com.spring.jwt.serviceImpl;

import com.spring.jwt.dto.EmployeePaymentsDTO;
import com.spring.jwt.entity.EmployeePayments;
import com.spring.jwt.repository.EmployeePaymentsRepository;
import com.spring.jwt.service.EmployeePaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeePaymentsServiceImpl implements EmployeePaymentsService {

    @Autowired
    private EmployeePaymentsRepository repository;

    private EmployeePaymentsDTO toDTO(EmployeePayments entity) {
        EmployeePaymentsDTO dto = new EmployeePaymentsDTO();
        dto.setId(entity.getId());
        dto.setJoiningDate(entity.getJoiningDate());
        dto.setName(entity.getName());
        dto.setSalary(entity.getSalary());
        dto.setAdvancePayment(entity.getAdvancePayment());
        return dto;
    }

    private EmployeePayments toEntity(EmployeePaymentsDTO dto) {
        EmployeePayments entity = new EmployeePayments();
        entity.setId(dto.getId());
        entity.setJoiningDate(dto.getJoiningDate());
        entity.setName(dto.getName());
        entity.setSalary(dto.getSalary());
        entity.setAdvancePayment(dto.getAdvancePayment());
        return entity;
    }

    @Override
    public EmployeePaymentsDTO createEmployee(EmployeePaymentsDTO dto) {
        EmployeePayments saved = repository.save(toEntity(dto));
        return toDTO(saved);
    }

    @Override
    public EmployeePaymentsDTO getEmployeeById(Integer id) {
        EmployeePayments employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return toDTO(employee);
    }

    @Override
    public List<EmployeePaymentsDTO> getAllEmployees() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeePaymentsDTO updateEmployeePayments(Integer id, Integer salary, Integer advancePayment) {
        EmployeePayments employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (salary != null) employee.setSalary(salary);
        if (advancePayment != null) employee.setAdvancePayment(advancePayment);

        return toDTO(repository.save(employee));
    }
}
