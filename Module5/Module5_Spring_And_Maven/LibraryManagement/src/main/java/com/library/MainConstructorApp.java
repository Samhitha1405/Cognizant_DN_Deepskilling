package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Covers Exercise 7 (constructor and setter injection).
 * Uses applicationContext-constructor.xml (constructor-arg wiring).
 * For the setter-injection half of Exercise 7, see MainXmlApp / applicationContext.xml.
 */
public class MainConstructorApp {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext-constructor.xml");

        BookService bookService = context.getBean("bookService", BookService.class);
        System.out.println(bookService.getBookDetails(103));
    }
}
