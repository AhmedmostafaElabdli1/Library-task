package com.code81.library.Repository;

import com.code81.library.Entity.Book;
import com.code81.library.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findByIdWithLock(Long id);

    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name = :authorName")
    List<Book> findByAuthorName(String authorName);

    List<Book> findByRemainingQuantityGreaterThan(int quantity);

    boolean existsByIsbn(String isbn);

    // Find all books by author ID
    List<Book> findByAuthors_Id(Long authorId);

    // Find all books by publisher ID
    List<Book> findByPublisher_Id(Long publisherId);
}