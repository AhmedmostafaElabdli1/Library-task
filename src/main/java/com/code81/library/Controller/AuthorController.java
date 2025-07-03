package com.code81.library.Controller;

import com.code81.library.DTO.AuthorDTO;
import com.code81.library.Service.AuthorService;
import com.code81.library.Util.UserActivityLogger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final UserActivityLogger userActivityLogger;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO dto) {
        AuthorDTO created = authorService.createAuthor(dto);
        userActivityLogger.log("Created author: " + created.getName());
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        userActivityLogger.log("Viewed all authors");
        return ResponseEntity.ok(authors);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id)
                .map(author -> {
                    userActivityLogger.log("Viewed author with ID: " + id);
                    return ResponseEntity.ok(author);
                })
                .orElseGet(() -> {
                    userActivityLogger.log("Attempted to view non-existing author with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Update
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO dto) {
        return authorService.updateAuthor(id, dto)
                .map(updated -> {
                    userActivityLogger.log("Updated author with ID: " + id);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    userActivityLogger.log("Attempted to update non-existing author with ID: " + id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Delete
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable String Email) {
        if (authorService.deleteAuthor(Email)) {
            userActivityLogger.log("Deleted author with Email: " +Email );
            return ResponseEntity.ok().build();
        } else {
            userActivityLogger.log("Attempted to delete non-existing author with Email: " + Email);
            return ResponseEntity.notFound().build();
        }
    }
}
