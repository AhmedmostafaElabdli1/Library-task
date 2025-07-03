package com.code81.library.Repository;

import com.code81.library.Entity.Author;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByName(String name);
    boolean existsByEmail(String email);
    List<Author> findByNameContainingIgnoreCase(String name);

    void deleteByEmail(String email);


//
//    // Bulk operations
//    @Modifying
//    @Query("UPDATE Author a SET a.nationality = :nationality WHERE a.id IN :ids")
//    int updateNationalityByIds(
//            @Param("nationality") String nationality,
//            @Param("ids") List<Long> ids
//    );

}