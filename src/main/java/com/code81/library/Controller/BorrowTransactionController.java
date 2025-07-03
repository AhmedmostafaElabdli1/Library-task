package com.code81.library.Controller;

import com.code81.library.DTO.BorrowTransactionDTO;
import com.code81.library.Entity.Borrower;
import com.code81.library.Mapper.BorrowTransactionMapper;
import com.code81.library.Repository.BookRepository;
import com.code81.library.Repository.BorrowerRepository;
import com.code81.library.Service.BorrowerService;
import com.code81.library.Util.UserActivityLogger;
import com.code81.library.model.BorrowTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class BorrowTransactionController {

    private final BorrowerService borrowerService;
    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final UserActivityLogger userActivityLogger;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PostMapping("/borrow")
    public ResponseEntity<BorrowTransactionDTO> borrowBook(
            @RequestParam Long borrowerId,
            @RequestParam Long bookId
    ) {
        BorrowTransaction transaction = borrowerService.borrowBook(borrowerId, bookId);
        userActivityLogger.log("Borrower ID " + borrowerId + " borrowed book ID " + bookId);
        return ResponseEntity.ok(BorrowTransactionMapper.toDTO(transaction));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @PostMapping("/return/{transactionId}")
    public ResponseEntity<BorrowTransactionDTO> returnBook(@PathVariable Long transactionId) {
        BorrowTransaction transaction = borrowerService.returnBook(transactionId);
        userActivityLogger.log("Returned book transaction ID " + transactionId);
        return ResponseEntity.ok(BorrowTransactionMapper.toDTO(transaction));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping
    public ResponseEntity<List<BorrowTransactionDTO>> getAllTransactions() {
        List<BorrowTransaction> allTransactions = borrowerRepository.findAll().stream()
                .flatMap(b -> b.getTransactionHistory().stream())
                .filter(tx -> isAllowedForStaff(tx)) // staff can't see completed
                .toList();

        List<BorrowTransactionDTO> result = allTransactions.stream()
                .map(BorrowTransactionMapper::toDTO)
                .collect(Collectors.toList());

        userActivityLogger.log("Viewed all borrow transactions");
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<List<BorrowTransactionDTO>> getBorrowerTransactions(@PathVariable Long borrowerId) {
        List<BorrowTransaction> all = borrowerService.getBorrowerHistory(borrowerId);
        List<BorrowTransactionDTO> result = all.stream()
                .filter(this::isAllowedForStaff)
                .map(BorrowTransactionMapper::toDTO)
                .collect(Collectors.toList());

        userActivityLogger.log("Viewed borrow transactions for borrower ID " + borrowerId);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long transactionId) {
        userActivityLogger.log("Attempted to delete transaction ID " + transactionId + " (not allowed)");
        return ResponseEntity.status(405).build(); // Not allowed by default
    }

    private boolean isAllowedForStaff(BorrowTransaction tx) {
        return !tx.isReturned() || hasRole("ADMINISTRATOR") || hasRole("LIBRARIAN");
    }

    private boolean hasRole(String role) {
        return org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }
}
