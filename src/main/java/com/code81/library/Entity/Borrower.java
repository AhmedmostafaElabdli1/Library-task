package com.code81.library.Entity;

import com.code81.library.model.BorrowTransaction;
import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "borrowers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 14)
    private String nid;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @jakarta.validation.constraints.Pattern(
            regexp = "^01[0125][0-9]{8}$",
            message = "Phone number must be a valid Egyptian mobile number")
    private String phone;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<BorrowTransaction> transactionHistory = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;
    public void addTransaction(BorrowTransaction transaction) {
        transactionHistory.add(transaction);
        transaction.setBorrower(this);
    }
}
