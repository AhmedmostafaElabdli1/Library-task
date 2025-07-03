package com.code81.library.Service;

import com.code81.library.Entity.Book;
import com.code81.library.Entity.Borrower;
import com.code81.library.Exception.EmailAlreadyExistsException;
import com.code81.library.Exception.ResourceNotFound;
import com.code81.library.model.BorrowTransaction;
import com.code81.library.Repository.BookRepository;
import com.code81.library.Repository.BorrowerRepository;
import com.code81.library.Repository.BorrowTransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BookRepository bookRepository;
    private final BorrowTransactionRepository transactionRepository;

    public Borrower create(Borrower borrower) {
        if (borrowerRepository.existsByEmail(borrower.getEmail())) {
            throw new EmailAlreadyExistsException("Email already used by another Borrower : " + borrower.getEmail()+" or Borrower added before");
        }
        return borrowerRepository.save(borrower);
    }

    public List<Borrower> getAll() {
        return borrowerRepository.findAll();
    }

    public Borrower getById(Long id) {
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
    }

    public void delete(Long id) {
        if (!borrowerRepository.existsById(id)) {
            throw new ResourceNotFound("Borrower not found");
        }
        borrowerRepository.deleteById(id);
    }

    @Transactional
    public BorrowTransaction borrowBook(Long borrowerId, Long bookId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getRemainingQuantity() <= 0) {
            throw new RuntimeException("No available copies for this book.");
        }

        // Decrement remaining and increment borrowedCount
        book.setRemainingQuantity(book.getRemainingQuantity() - 1);
        book.setBorrowedCount(book.getBorrowedCount() + 1);

        bookRepository.save(book);

        BorrowTransaction transaction = BorrowTransaction.builder()
                .borrower(borrower)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returned(false)
                .build();

        return transactionRepository.save(transaction);
    }


    @Transactional
    public BorrowTransaction returnBook(Long transactionId) {
        BorrowTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFound("Transaction not found"));

        if (transaction.isReturned()) {
            throw new RuntimeException("Book already returned.");
        }

        transaction.setReturned(true);
        transaction.setReturnDate(LocalDate.now());

        Book book = transaction.getBook();

        // Increment remaining and decrement borrowedCount
        book.setRemainingQuantity(book.getRemainingQuantity() + 1);
        book.setBorrowedCount(book.getBorrowedCount() - 1);

        bookRepository.save(book);

        return transactionRepository.save(transaction);
    }


    public List<BorrowTransaction> getBorrowerHistory(Long borrowerId) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResourceNotFound("Borrower not found"));
        return borrower.getTransactionHistory();
    }
}
