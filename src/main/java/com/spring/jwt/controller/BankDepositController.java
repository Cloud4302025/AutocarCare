package com.spring.jwt.controller;

import com.spring.jwt.dto.BankDepositDTO;
import com.spring.jwt.service.BankDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/deposits")
public class BankDepositController {

    @Autowired
    private BankDepositService depositService;

    @PostMapping("/add")
    public BankDepositDTO create(@RequestBody BankDepositDTO dto) {
        return depositService.createDeposit(dto);
    }

    @GetMapping("/getById/{id}")
    public BankDepositDTO getById(@PathVariable Long id) {
        return depositService.getDepositById(id);
    }

    @GetMapping("/getAll")
    public List<BankDepositDTO> getAll() {
        return depositService.getAllDeposits();
    }

    @PatchMapping("/update/{id}")
    public BankDepositDTO updateAmount(@PathVariable Long id,
                                       @RequestParam Integer newAmount) {
        return depositService.updateAmount(id, newAmount);
    }

    @GetMapping("/between")
    public List<BankDepositDTO> findBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return depositService.findDepositsBetweenDates(start, end);
    }
}
