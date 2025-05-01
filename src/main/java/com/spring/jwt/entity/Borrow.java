package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name= "borrow")
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer borrowId;

    private String customerName;

    private LocalDate remainingDate;

    private Long remainingPayment;

}
