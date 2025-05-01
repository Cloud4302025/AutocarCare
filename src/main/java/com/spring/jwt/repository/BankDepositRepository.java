package com.spring.jwt.repository;

import com.spring.jwt.entity.BankDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankDepositRepository extends JpaRepository<BankDeposit, Long> {

    List<BankDeposit> findByDepositDateBetween(LocalDate startDate, LocalDate endDate);
}