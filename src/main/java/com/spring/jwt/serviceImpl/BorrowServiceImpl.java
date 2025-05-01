package com.spring.jwt.serviceImpl;

import com.spring.jwt.entity.Borrow;
import com.spring.jwt.dto.BorrowDTO;
import com.spring.jwt.repository.BorrowRepository;
import com.spring.jwt.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Autowired
    private BorrowRepository borrowRepository;

    private BorrowDTO convertToDTO(Borrow borrow) {
        BorrowDTO dto = new BorrowDTO();
        dto.setBorrowId(borrow.getBorrowId());
        dto.setCustomerName(borrow.getCustomerName());
        dto.setRemainingDate(borrow.getRemainingDate());
        dto.setRemainingPayment(borrow.getRemainingPayment());
        return dto;
    }

    private Borrow convertToEntity(BorrowDTO dto) {
        Borrow borrow = new Borrow();
        borrow.setBorrowId(dto.getBorrowId());
        borrow.setCustomerName(dto.getCustomerName());
        borrow.setRemainingDate(dto.getRemainingDate());
        borrow.setRemainingPayment(dto.getRemainingPayment());
        return borrow;
    }

    @Override
    public BorrowDTO createBorrow(BorrowDTO borrowDTO) {
        Borrow saved = borrowRepository.save(convertToEntity(borrowDTO));
        return convertToDTO(saved);
    }

    @Override
    public BorrowDTO getBorrowById(Integer id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow not found"));
        return convertToDTO(borrow);
    }

    @Override
    public List<BorrowDTO> getAllBorrows() {
        return borrowRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BorrowDTO updateRemainingPayment(Integer id, Long newPayment) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrow not found"));
        borrow.setRemainingPayment(newPayment);
        return convertToDTO(borrowRepository.save(borrow));
    }
}

