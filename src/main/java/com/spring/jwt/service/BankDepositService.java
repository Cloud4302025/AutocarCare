package com.spring.jwt.service;

import com.spring.jwt.dto.BankDepositDTO;

import java.time.LocalDate;
import java.util.List;

public interface BankDepositService {
    BankDepositDTO createDeposit(BankDepositDTO dto);
    BankDepositDTO getDepositById(Long id);
    List<BankDepositDTO> getAllDeposits();
    BankDepositDTO updateAmount(Long id, Integer newAmount);
    List<BankDepositDTO> findDepositsBetweenDates(LocalDate startDate, LocalDate endDate);
}