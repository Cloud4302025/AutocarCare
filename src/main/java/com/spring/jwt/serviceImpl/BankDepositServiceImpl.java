package com.spring.jwt.serviceImpl;

import com.spring.jwt.dto.BankDepositDTO;
import com.spring.jwt.entity.BankDeposit;
import com.spring.jwt.repository.BankDepositRepository;
import com.spring.jwt.service.BankDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankDepositServiceImpl implements BankDepositService {

    @Autowired
    private BankDepositRepository depositRepository;

    private BankDepositDTO toDTO(BankDeposit deposit) {
        BankDepositDTO dto = new BankDepositDTO();
        dto.setId(deposit.getId());
        dto.setDepositDate(deposit.getDepositDate());
        dto.setAmount(deposit.getAmount());
        return dto;
    }

    private BankDeposit toEntity(BankDepositDTO dto) {
        BankDeposit deposit = new BankDeposit();
        deposit.setId(dto.getId());
        deposit.setDepositDate(dto.getDepositDate());
        deposit.setAmount(dto.getAmount());
        return deposit;
    }

    @Override
    public BankDepositDTO createDeposit(BankDepositDTO dto) {
        BankDeposit saved = depositRepository.save(toEntity(dto));
        return toDTO(saved);
    }

    @Override
    public BankDepositDTO getDepositById(Long id) {
        BankDeposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deposit not found"));
        return toDTO(deposit);
    }

    @Override
    public List<BankDepositDTO> getAllDeposits() {
        return depositRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BankDepositDTO updateAmount(Long id, Integer newAmount) {
        BankDeposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deposit not found"));
        deposit.setAmount(newAmount);
        return toDTO(depositRepository.save(deposit));
    }

    @Override
    public List<BankDepositDTO> findDepositsBetweenDates(LocalDate start, LocalDate end) {
        return depositRepository.findByDepositDateBetween(start, end)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
}
