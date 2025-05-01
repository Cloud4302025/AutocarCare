package com.spring.jwt.repository;

import com.spring.jwt.entity.EmployeePayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeePaymentsRepository extends JpaRepository<EmployeePayments, Integer> {
}