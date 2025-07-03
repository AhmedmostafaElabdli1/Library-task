package com.code81.library.Repository;



import com.code81.library.model.BorrowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowTransactionRepository extends JpaRepository<BorrowTransaction, Long> {
}

