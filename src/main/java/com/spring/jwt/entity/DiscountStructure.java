package com.spring.jwt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class DiscountStructure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Integer discountId;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "discount_a")
    private Integer discountA;

    @Column(name = "discount_b")
    private Integer discountB;

    @Column(name = "discount_c")
    private Integer discountC;

    @Column(name = "active_set_index")
    private Integer activeSetIndex;
}

