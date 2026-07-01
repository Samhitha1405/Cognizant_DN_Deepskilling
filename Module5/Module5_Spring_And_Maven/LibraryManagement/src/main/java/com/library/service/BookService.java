package com.library.service;

import com.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private BookRepository bookRepository;

    // No-arg constructor (needed for setter injection / annotation scanning)
    public BookService() {
    }

    // Constructor injection (used by Exercise 7 - constructor wiring)
    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Setter injection (used by Exercises 1, 2, 5, 7 - setter wiring)
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public String getBookDetails(int id) {
        return bookRepository.findBookById(id);
    }
}
