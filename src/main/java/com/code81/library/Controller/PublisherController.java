package com.code81.library.Controller;

import com.code81.library.Entity.Publisher;
import com.code81.library.Repository.PublisherRepository;
import com.code81.library.Util.UserActivityLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherRepository publisherRepository;
    private final UserActivityLogger userActivityLogger;

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        userActivityLogger.log("Viewed all publishers");
        return ResponseEntity.ok(publisherRepository.findAll());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PostMapping
    public ResponseEntity<Publisher> createPublisher(@RequestBody Publisher publisher) {
        if (publisherRepository.existsByName(publisher.getName())) {
            userActivityLogger.log("Attempted to create publisher with existing name: " + publisher.getName());
            return ResponseEntity.badRequest().build();
        }
        Publisher saved = publisherRepository.save(publisher);
        userActivityLogger.log("Created publisher: " + saved.getName());
        return ResponseEntity.ok(saved);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN', 'STAFF')")
    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        var publisherOpt = publisherRepository.findById(id);
        if (publisherOpt.isPresent()) {
            userActivityLogger.log("Viewed publisher with ID: " + id);
            return ResponseEntity.ok(publisherOpt.get());
        } else {
            userActivityLogger.log("Attempted to view non-existing publisher with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'LIBRARIAN')")
    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable Long id, @RequestBody Publisher updated) {
        return publisherRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setEmail(updated.getEmail());
            Publisher saved = publisherRepository.save(existing);
            userActivityLogger.log("Updated publisher with ID: " + id);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> {
            userActivityLogger.log("Attempted to update non-existing publisher with ID: " + id);
            return ResponseEntity.notFound().build();
        });
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        if (!publisherRepository.existsById(id)) {
            userActivityLogger.log("Attempted to delete non-existing publisher with ID: " + id);
            return ResponseEntity.notFound().build();
        }
        publisherRepository.deleteById(id);
        userActivityLogger.log("Deleted publisher with ID: " + id);
        return ResponseEntity.noContent().build();
    }
}
