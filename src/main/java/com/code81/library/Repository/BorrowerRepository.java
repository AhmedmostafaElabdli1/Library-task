package com.code81.library.Repository;

import com.code81.library.Entity.Borrower;
import com.code81.library.model.BorrowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    @Query("SELECT b FROM Borrower b WHERE b.id = :id")
    Optional<Borrower> findByIdWithLock(Long id);

    Optional<Borrower> findByEmail(String email);

    @Query("SELECT bt FROM BorrowTransaction bt WHERE bt.borrower = :borrower AND bt.returned = false")
    List<BorrowTransaction> findActiveTransactions(Borrower borrower);

    @Query("SELECT COUNT(bt) > 0 FROM BorrowTransaction bt WHERE bt.borrower = :borrower AND bt.dueDate < :date AND bt.returned = false")
    boolean hasOverdueBooks(Borrower borrower, LocalDate date);

    boolean existsByEmail(String email);
}