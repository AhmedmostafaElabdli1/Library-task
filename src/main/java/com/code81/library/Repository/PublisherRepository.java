package com.code81.library.Repository;

import com.code81.library.Entity.Publisher;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    // Basic queries
    boolean existsByName(String name);

    Optional<Publisher> findByName(String name);


    boolean existsByEmail(@Email String email);

    Optional<Publisher> findByEmail(String email);
}