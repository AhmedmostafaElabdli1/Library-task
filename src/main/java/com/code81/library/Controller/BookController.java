package com.code81.library.Controller;

import com.code81.library.Entity.Book;
import com.code81.library.Service.BookService;
import com.code81.library.DTO.BookDTO;
import com.code81.library.DTO.BookRequestDto;
import com.code81.library.Util.UserActivityLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserActivityLogger userActivityLogger;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        userActivityLogger.log("Viewed all books");
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(book -> {
                    userActivityLogger.log("Viewed book with ID: " + id);
                    return ResponseEntity.ok(book);
                })
                .orElseGet(() -> {
                    userActivityLogger.log("Attempted to view non-existing book with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody BookRequestDto dto) {
        BookDTO created = bookService.createBook(dto);
        userActivityLogger.log("Created book: " + created.getTitle());
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookRequestDto dto) {
        BookDTO updated = bookService.updateBook(id, dto);
        userActivityLogger.log("Updated book with ID: " + id);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        userActivityLogger.log("Deleted book with ID: " + id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long authorId) {
        List<Book> books = bookService.getBooksByAuthorId(authorId);
        userActivityLogger.log("Viewed books by author ID: " + authorId);
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<List<Book>> getBooksByPublisher(@PathVariable Long publisherId) {
        List<Book> books = bookService.getBooksByPublisherId(publisherId);
        userActivityLogger.log("Viewed books by publisher ID: " + publisherId);
        return ResponseEntity.ok(books);
    }
}
