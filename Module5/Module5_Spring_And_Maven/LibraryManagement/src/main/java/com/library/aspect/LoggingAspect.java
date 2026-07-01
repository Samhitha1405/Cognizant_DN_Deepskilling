package com.library.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class LoggingAspect {

    // Advice: runs before any method in BookService executes
    @Before("execution(* com.library.service.BookService.*(..))")
    public void logBefore() {
        System.out.println("[LOG] Before method execution...");
    }

    // Advice: runs after any method in BookService executes (regardless of outcome)
    @After("execution(* com.library.service.BookService.*(..))")
    public void logAfter() {
        System.out.println("[LOG] After method execution...");
    }

    // Advice: wraps the method call to measure and log execution time
    @Around("execution(* com.library.service.BookService.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        System.out.println("[LOG] " + joinPoint.getSignature()
                + " executed in " + (end - start) + " ms");
        return result;
    }
}
