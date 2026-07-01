package com.library;

import com.library.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Covers Exercise 3 (logging with Spring AOP) and Exercise 8 (basic AOP - before/after advice).
 * Uses applicationContext-aop.xml (registers LoggingAspect + aspectj-autoproxy).
 * Watch the console - you'll see [LOG] messages printed before, after,
 * and with the execution time of getBookDetails().
 */
public class MainAopApp {
    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("applicationContext-aop.xml");

        BookService bookService = context.getBean("bookService", BookService.class);
        System.out.println(bookService.getBookDetails(104));
    }
}
