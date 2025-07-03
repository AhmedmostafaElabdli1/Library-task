package com.code81.library.Mapper;

import com.code81.library.DTO.BorrowTransactionDTO;
import com.code81.library.model.BorrowTransaction;

public class BorrowTransactionMapper {

    public static BorrowTransactionDTO toDTO(BorrowTransaction tx) {
        return BorrowTransactionDTO.builder()
                .transactionId(tx.getId())
                .bookTitle(tx.getBook().getTitle())
                .borrowerName(tx.getBorrower().getName())
                .borrowDate(tx.getBorrowDate())
                .dueDate(tx.getDueDate())
                .returnDate(tx.getReturnDate())
                .returned(tx.isReturned())
                .createdAt(tx.getCreatedAt())
                .updatedAt(tx.getUpdatedAt())
                .createdBy(tx.getCreatedBy())
                .updatedBy(tx.getUpdatedBy())
                .build();
    }
}
