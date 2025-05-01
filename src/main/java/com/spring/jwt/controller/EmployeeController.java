package com.spring.jwt.controller;

import com.spring.jwt.dto.EmployeeDTO;
import com.spring.jwt.service.EmployeeService;
import com.spring.jwt.utils.BaseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/add")
    public BaseResponseDTO createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        BaseResponseDTO savedEmployee = employeeService.createEmployee(employeeDTO);
        return new BaseResponseDTO( "200", "Ok");
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployeePartially(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        EmployeeDTO updatedEmployee = employeeService.updateEmployeePartially(id, updates);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee successfully deleted");
    }

}
