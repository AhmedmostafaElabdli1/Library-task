package com.code81.library.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BorrowerDTO {
    private Long id;
    private String name;
    private String nid;
    private String email;
    private String phone;
    private String address;
    private List<BorrowTransactionDTO> transactionHistory;

    // 🕒 Auditing
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}

