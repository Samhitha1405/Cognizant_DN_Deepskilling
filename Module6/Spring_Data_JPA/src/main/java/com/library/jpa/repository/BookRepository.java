package com.library.jpa.repository;

import com.library.jpa.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // DERIVED QUERY METHOD:
    // Spring Data JPA parses this method name and builds the query automatically:
    // SELECT * FROM book WHERE title = ?
    List<Book> findByTitle(String title);

    // Derived query with a relationship field (author.name) and a "Containing" keyword:
    // SELECT * FROM book b JOIN author a ON b.author_id = a.id WHERE a.name LIKE %?%
    List<Book> findByAuthorNameContaining(String namePart);

    // Derived query using a comparison keyword (GreaterThan):
    // SELECT * FROM book WHERE publication_year > ?
    List<Book> findByPublicationYearGreaterThan(int year);

    // CUSTOM JPQL QUERY:
    // Use this when the method-name approach gets too complex or unreadable.
    // Note: this operates on the ENTITY name "Book" and field "title", not the table/column names.
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword%")
    List<Book> searchByTitleKeyword(@Param("keyword") String keyword);
}
