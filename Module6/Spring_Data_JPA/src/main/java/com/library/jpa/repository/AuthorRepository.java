package com.library.jpa.repository;

import com.library.jpa.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    // JpaRepository already gives us: save(), findAll(), findById(), deleteById(), count(), etc.
    // No implementation needed - Spring Data JPA generates it at runtime.
}
