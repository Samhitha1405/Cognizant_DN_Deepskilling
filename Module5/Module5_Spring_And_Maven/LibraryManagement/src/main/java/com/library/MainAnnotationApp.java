package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Covers Exercise 6 (annotation-based bean configuration).
 * Uses applicationContext-annotation.xml (component-scan + @Service/@Repository/@Autowired).
 */
public class MainAnnotationApp {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext-annotation.xml");

        BookService bookService = context.getBean("bookService", BookService.class);
        System.out.println(bookService.getBookDetails(102));
    }
}
