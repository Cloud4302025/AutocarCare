package com.spring.jwt.controller;

import com.spring.jwt.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    @Schema(description = "Email of the customer", example = "example@example.com")
    private String email;

    @Schema(description = "Password of the customer", example = "Pass@1234")
    private String password;

    @Schema(description = "Customer's full name (stored in firstName only)", example = "John Doe")
    private String name;

    @Schema(description = "Customer's address", example = "123 Main St, Pune")
    private String address;

    @Schema(description = "Mobile number of the customer", example = "9876543210")
    private String mobile;

    @Schema(description = "Aadhar number of the customer", example = "123456789012")
    private String aadhar;

    @Schema(description = "GSTIN number of the customer", example = "29ABCDE1234F2Z5")
    private String gstin;

    @Schema(description = "Role of the customer", example = "USER")
    private String role;

    public CustomerDTO(User user) {
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.mobile = user.getMobileNumber() != null ? String.valueOf(user.getMobileNumber()) : null;
        this.name = user.getFirstName();
        this.aadhar = user.getAdharNo() != null ? String.valueOf(user.getAdharNo()) : null;
        this.gstin = user.getGSTINNo();
    }

    public User toEntity() {
        User user = new User();
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setAddress(this.address);

        user.setFirstName(this.name);
        user.setLastName(null);

        if (this.mobile != null && !this.mobile.isBlank()) {
            user.setMobileNumber(Long.parseLong(this.mobile));
        }

        if (this.aadhar != null && !this.aadhar.isBlank()) {
            user.setAdharNo(Integer.parseInt(this.aadhar));
        }

        user.setGSTINNo(this.gstin);
        return user;
    }
}

