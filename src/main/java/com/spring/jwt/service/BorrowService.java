package com.spring.jwt.service;

import com.spring.jwt.dto.BorrowDTO;

import java.util.List;

public interface BorrowService {
    BorrowDTO createBorrow(BorrowDTO borrowDTO);
    BorrowDTO getBorrowById(Integer id);
    List<BorrowDTO> getAllBorrows();
    BorrowDTO updateRemainingPayment(Integer id, Long newPayment);
}