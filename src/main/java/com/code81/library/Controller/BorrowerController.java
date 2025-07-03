package com.code81.library.Controller;

import com.code81.library.DTO.BorrowerDTO;
import com.code81.library.Entity.Borrower;
import com.code81.library.Mapper.BorrowerMapper;
import com.code81.library.Service.BorrowerService;
import com.code81.library.Util.UserActivityLogger;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;
    private final UserActivityLogger userActivityLogger;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PostMapping
    public ResponseEntity<Borrower> createBorrower(@Valid @RequestBody Borrower borrower) {
        Borrower created = borrowerService.create(borrower);
        userActivityLogger.log("Created borrower: " + created.getName());
        return ResponseEntity.ok(created);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping
    public ResponseEntity<List<BorrowerDTO>> getAllBorrowers() {
        List<BorrowerDTO> dtos = borrowerService.getAll()
                .stream()
                .map(BorrowerMapper::toDTO)
                .collect(Collectors.toList());
        userActivityLogger.log("Viewed all borrowers");
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<Borrower> getBorrowerById(@PathVariable Long id) {
        userActivityLogger.log("Viewed borrower with ID: " + id);
        return ResponseEntity.ok(borrowerService.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<Borrower> updateBorrower(@PathVariable Long id, @Valid @RequestBody Borrower updatedBorrower) {
        Borrower existing = borrowerService.getById(id);
        existing.setName(updatedBorrower.getName());
        existing.setNid(updatedBorrower.getNid());
        existing.setEmail(updatedBorrower.getEmail());
        existing.setPhone(updatedBorrower.getPhone());
        existing.setAddress(updatedBorrower.getAddress());
        Borrower saved = borrowerService.create(existing);
        userActivityLogger.log("Updated borrower with ID: " + id);
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable Long id) {
        borrowerService.delete(id);
        userActivityLogger.log("Deleted borrower with ID: " + id);
        return ResponseEntity.noContent().build();
    }
}
