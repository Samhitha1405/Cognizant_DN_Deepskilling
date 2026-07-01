package com.library.jpa.controller;

import com.library.jpa.entity.Book;
import com.library.jpa.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book book = bookRepository.findById(id).orElseThrow();
        book.setTitle(updatedBook.getTitle());
        book.setIsbn(updatedBook.getIsbn());
        book.setPublicationYear(updatedBook.getPublicationYear());
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }

    // --- Demonstrating derived query methods & JPQL ---

    @GetMapping("/search/by-title")
    public List<Book> findByTitle(@RequestParam String title) {
        return bookRepository.findByTitle(title);
    }

    @GetMapping("/search/by-author")
    public List<Book> findByAuthor(@RequestParam String name) {
        return bookRepository.findByAuthorNameContaining(name);
    }

    @GetMapping("/search/after-year")
    public List<Book> findAfterYear(@RequestParam int year) {
        return bookRepository.findByPublicationYearGreaterThan(year);
    }

    @GetMapping("/search/jpql")
    public List<Book> searchJpql(@RequestParam String keyword) {
        return bookRepository.searchByTitleKeyword(keyword);
    }
}
