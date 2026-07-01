package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Covers Exercise 1 (basic config), Exercise 2 (dependency injection),
 * Exercise 5 (IoC container configuration).
 * Uses applicationContext.xml (setter injection, no component scan).
 */
public class MainXmlApp {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        BookService bookService = context.getBean("bookService", BookService.class);
        System.out.println(bookService.getBookDetails(101));
    }
}
