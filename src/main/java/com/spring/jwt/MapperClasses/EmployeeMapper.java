package com.spring.jwt.MapperClasses;

import com.spring.jwt.dto.EmployeeDTO;
import com.spring.jwt.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDTO toDTO(Employee employee) {
        if (employee == null) return null;
        return EmployeeDTO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .position(employee.getPosition())
                .contact(employee.getContact())
                .address(employee.getAddress())
                .email(employee.getEmail())
                .password(employee.getPassword())
                .username(employee.getUsername())
                .componentNames(employee.getComponentNames())
                .build();
    }

    public static Employee toEntity(EmployeeDTO dto) {
        if (dto == null) return null;
        return Employee.builder()
                .id(dto.getId())
                .name(dto.getName())
                .position(dto.getPosition())
                .contact(dto.getContact())
                .address(dto.getAddress())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .componentNames(dto.getComponentNames())
                .build();
    }
}
