package com.spring.jwt.dto;

import com.spring.jwt.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id;
    private String name;
    private String position;
    private String contact;
    private String address;
    private String email;
    private String username;
    private Integer userId;
    private String password;
    private String role;
    private List<String> componentNames;

    public User toEntity() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setAddress(this.address);

        user.setFirstName(this.name);
        user.setLastName(null);
        return user;
    }
}

