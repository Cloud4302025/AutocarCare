package com.spring.jwt.controller;

import com.spring.jwt.dto.BorrowDTO;
import com.spring.jwt.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping("/add")
    public BorrowDTO createBorrow(@RequestBody BorrowDTO borrowDTO) {
        return borrowService.createBorrow(borrowDTO);
    }

    @GetMapping("/getById/{id}")
    public BorrowDTO getBorrowById(@PathVariable Integer id) {
        return borrowService.getBorrowById(id);
    }

    @GetMapping("/getAll")
    public List<BorrowDTO> getAllBorrows() {
        return borrowService.getAllBorrows();
    }

    @PatchMapping("/update/{id}")
    public BorrowDTO updateRemainingPayment(@PathVariable Integer id,
                                            @RequestParam Long newPayment) {
        return borrowService.updateRemainingPayment(id, newPayment);
    }
}
