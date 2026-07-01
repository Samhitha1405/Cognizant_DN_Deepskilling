README - Module 5: Spring and Maven Exercises
================================================

This zip contains TWO Maven projects, matching what IntelliJ expects
(each top-level folder = one Maven module). Unzip and place both
folders directly inside your "Module5 Spring And Maven" directory.

------------------------------------------------------------------
PROJECT 1: LibraryManagement  (covers Exercises 1, 2, 3, 4, 5, 6, 7, 8)
------------------------------------------------------------------
Classic Spring Framework (XML + annotations + AOP), one Maven project,
multiple runnable entry points so each exercise can be tested independently.

Files:
  pom.xml
    -> Exercise 4 (Maven setup: spring-context, spring-aop, spring-webmvc,
       Java 1.8 compiler plugin)

  src/main/java/com/library/repository/BookRepository.java
  src/main/java/com/library/service/BookService.java
    -> Exercise 1 & 4 (service/repository classes)
       BookService has BOTH a constructor and a setter for BookRepository,
       so it works with every injection style used across the exercises.

  src/main/java/com/library/aspect/LoggingAspect.java
    -> Exercise 3 & 8 (AOP logging aspect: @Before, @After, @Around)

  src/main/resources/applicationContext.xml
    -> Exercise 1, 2, 5 (setter-injection XML wiring)
       Run: MainXmlApp.java

  src/main/resources/applicationContext-annotation.xml
    -> Exercise 6 (component-scan + @Service/@Repository/@Autowired)
       Run: MainAnnotationApp.java

  src/main/resources/applicationContext-constructor.xml
    -> Exercise 7 (constructor-injection XML wiring)
       Run: MainConstructorApp.java

  src/main/resources/applicationContext-aop.xml
    -> Exercise 3 & 8 (registers LoggingAspect + <aop:aspectj-autoproxy/>)
       Run: MainAopApp.java

How to run each one in IntelliJ:
  1. Right-click the relevant Main*.java file -> Run.
  2. Check the console output:
     - MainXmlApp / MainAnnotationApp / MainConstructorApp print:
         "Book with ID <number>"
     - MainAopApp additionally prints [LOG] lines before/after/around
       the method call, showing the AOP advice firing.

------------------------------------------------------------------
PROJECT 2: LibraryManagementBoot  (covers Exercise 9)
------------------------------------------------------------------
A separate Spring Boot project (different parent POM / packaging style,
which is why it's its own Maven module rather than mixed into the project above).

Files:
  pom.xml
    -> spring-boot-starter-parent, Web, Data JPA, H2 dependencies

  src/main/resources/application.properties
    -> H2 in-memory DB connection settings

  src/main/java/com/library/boot/entity/Book.java
    -> JPA entity (id, title, author, isbn)

  src/main/java/com/library/boot/repository/BookRepository.java
    -> Spring Data JPA repository interface

  src/main/java/com/library/boot/controller/BookController.java
    -> REST controller with full CRUD:
         POST   /api/books
         GET    /api/books
         GET    /api/books/{id}
         PUT    /api/books/{id}
         DELETE /api/books/{id}

  src/main/java/com/library/boot/LibraryManagementBootApplication.java
    -> @SpringBootApplication main class. Right-click -> Run.

Testing the REST endpoints:
  Once running, the app listens on http://localhost:8080
  Use Postman, curl, or IntelliJ's built-in HTTP client, e.g.:

    curl -X POST http://localhost:8080/api/books \
      -H "Content-Type: application/json" \
      -d '{"title":"Effective Java","author":"Joshua Bloch","isbn":"123"}'

    curl http://localhost:8080/api/books

  You can also browse the H2 console at http://localhost:8080/h2-console
  (JDBC URL: jdbc:h2:mem:librarydb, username: sa, no password).

------------------------------------------------------------------
Setting up in IntelliJ after unzipping
------------------------------------------------------------------
1. Unzip and place LibraryManagement/ and LibraryManagementBoot/ folders
   inside your "Module5 Spring And Maven" directory.
2. In IntelliJ: right-click each pom.xml -> "Add as Maven Project"
   (or File -> New -> Module from Existing Sources, pointing at each pom.xml).
3. Let Maven download dependencies (watch the progress bar bottom-right).
4. Mark each src/main/resources folder as a Resources Root if IntelliJ
   doesn't detect it automatically (right-click folder -> Mark Directory as
   -> Resources Root).
5. Run any of the Main*.java classes as described above.
