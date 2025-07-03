package com.code81.library.Mapper;

import com.code81.library.DTO.BorrowerDTO;
import com.code81.library.Entity.Borrower;

import java.util.stream.Collectors;


public class BorrowerMapper {
    public static BorrowerDTO toDTO(Borrower borrower) {
        return BorrowerDTO.builder()
                .id(borrower.getId())
                .name(borrower.getName())
                .nid(borrower.getNid())
                .email(borrower.getEmail())
                .phone(borrower.getPhone())
                .address(borrower.getAddress())
                .transactionHistory(
                        borrower.getTransactionHistory().stream()
                                .map(BorrowTransactionMapper::toDTO)
                                .collect(Collectors.toList()))
                // 🕒 Audit Info
                .createdAt(borrower.getCreatedAt())
                .updatedAt(borrower.getUpdatedAt())
                .createdBy(borrower.getCreatedBy())
                .updatedBy(borrower.getUpdatedBy())
                .build();
    }
}
