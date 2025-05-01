package com.spring.jwt.service;

import com.spring.jwt.MapperClasses.EmployeeMapper;
import com.spring.jwt.dto.EmployeeDTO;
import com.spring.jwt.entity.Employee;
import com.spring.jwt.entity.Role;
import com.spring.jwt.entity.User;
import com.spring.jwt.exception.ResourceNotFoundException;
import com.spring.jwt.repository.EmployeeRepository;
import com.spring.jwt.repository.RoleRepository;
import com.spring.jwt.repository.UserRepository;
import com.spring.jwt.utils.BaseResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final EmployeeRepository employeeRepository;

    @Transactional
    public BaseResponseDTO createEmployee(EmployeeDTO employeeDTO) {

        User existingUser = userRepository.findByEmail(employeeDTO.getEmail());
        if (existingUser != null) {
            return new BaseResponseDTO(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "User already registered with email: " + employeeDTO.getEmail()
            );
        }
        User user = new User();
        user.setFirstName(employeeDTO.getName());
        user.setLastName(null);
        user.setEmail(employeeDTO.getEmail());
        user.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        user.setAddress(employeeDTO.getAddress());
        user.setMobileNumber(Long.valueOf(employeeDTO.getContact()));

        Role employeeRole = roleRepository
                .findByName("EMPLOYEE");
        if(employeeRole== null){
                 roleRepository.save(new Role("EMPLOYEE"));
        }
        user.setRoles(Set.of(employeeRole));

        userRepository.save(user);

        Employee emp = Employee.builder()
                .position(employeeDTO.getPosition())
                .contact(employeeDTO.getContact())
                .address(employeeDTO.getAddress())
                .email(employeeDTO.getEmail())
                .name(employeeDTO.getName())
                .username(employeeDTO.getUsername())
                .password(employeeDTO.getPassword())
                .componentNames(employeeDTO.getComponentNames())
                .user(user)
                .build();
        employeeRepository.save(emp);

        return new BaseResponseDTO(
                String.valueOf(HttpStatus.OK.value()),
                "Employee created (userId=" + user.getId() +
                        ", employeeId=" + emp.getId() + ")"
        );
    }


    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());

    }

    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        return EmployeeMapper.toDTO(employee);
    }

    public EmployeeDTO updateEmployeePartially(Long id, Map<String, Object> updates) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        User user = employee.getUser();

        Map<String, BiConsumer<Employee, Object>> employeeUpdaters = Map.of(
                "name", (emp, value) -> emp.setName((String) value),
                "position", (emp, value) -> emp.setPosition((String) value),
                "contact", (emp, value) -> emp.setContact((String) value),
                "address", (emp, value) -> emp.setAddress((String) value),
                "email", (emp, value) -> emp.setEmail((String) value),
                "password", (emp, value) -> emp.setPassword((String) value),
                "username", (emp, value) -> emp.setUsername((String) value),
                "componentNames", (emp, value) -> emp.setComponentNames((List<String>) value)
        );

        Map<String, BiConsumer<User, Object>> userUpdaters = new HashMap<>();
        userUpdaters.put("email", (usr, value) -> usr.setEmail((String) value));
        userUpdaters.put("mobileNumber", (usr, value) -> usr.setMobileNumber(Long.valueOf(value.toString())));
        userUpdaters.put("address", (usr, value) -> usr.setAddress((String) value));
        userUpdaters.put("password", (usr, value) -> {
            String rawPassword = (String) value;
            String encrypted = passwordEncoder.encode(rawPassword);
            usr.setPassword(encrypted);
        });

        updates.forEach((key, value) -> {
            if (employeeUpdaters.containsKey(key)) {
                employeeUpdaters.get(key).accept(employee, value);
            } else if (userUpdaters.containsKey(key)) {
                userUpdaters.get(key).accept(user, value);
            }
        });

        userRepository.save(user);       // Save updated User
        Employee updatedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.toDTO(updatedEmployee);
    }



    public void deleteEmployee(Long id) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        employeeRepository.delete(existingEmployee);
        User byEmail = userRepository.findByEmail(existingEmployee.getEmail());
        byEmail.getRoles().clear();
        userRepository.save(byEmail);
        userRepository.deleteById(Long.valueOf(byEmail.getId()));


    }
}
