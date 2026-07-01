NOTES: SPRING DATA JPA
========================================================================

1. WHAT PROBLEM DOES THIS SOLVE?
------------------------------------------------------------------------
Without JPA, talking to a database from Java looks like this:

  Connection conn = DriverManager.getConnection(url, user, pass);
  PreparedStatement stmt = conn.prepareStatement("SELECT * FROM book WHERE id = ?");
  stmt.setLong(1, id);
  ResultSet rs = stmt.executeQuery();
  while (rs.next()) {
      Book b = new Book();
      b.setId(rs.getLong("id"));
      b.setTitle(rs.getString("title"));
      ...
  }

That's a lot of repetitive, error-prone boilerplate for every single table.
Spring Data JPA removes ALL of it. You write a Java class (the entity) and
a Java interface (the repository), and the framework generates the SQL,
opens the connection, maps rows back into objects, and closes everything
for you.

2. THE LAYERS (so the terminology stops being confusing)
------------------------------------------------------------------------
  JDBC          -> The lowest-level Java API for talking to ANY database.
                    Raw, verbose, manual.

  JPA           -> A SPECIFICATION (a set of interfaces/annotations like
                    @Entity, @Id, @OneToMany). It is just rules on paper -
                    it does not work by itself.

  Hibernate     -> An IMPLEMENTATION of the JPA specification. This is the
                    actual code that reads your @Entity classes and
                    generates real SQL. Spring Boot uses Hibernate by
                    default.

  Spring Data   -> A layer ON TOP of JPA/Hibernate. It looks at your
  JPA              repository INTERFACE and auto-generates the
                    implementation at runtime, so you never write a
                    single SQL statement for basic CRUD.

  Analogy: JDBC is like writing assembly. JPA is the rulebook for a
  higher-level language. Hibernate is the compiler for that language.
  Spring Data JPA is an code-generator that writes most of your program
  for you, based on naming conventions.

3. @ENTITY - MAPPING A CLASS TO A TABLE
------------------------------------------------------------------------
  @Entity                     -> marks the class as a database table
  @Id                          -> marks the primary key field
  @GeneratedValue(strategy=...) -> tells the DB to auto-increment the ID
  Plain fields (String, int)  -> become columns automatically, named
                                  after the field (camelCase becomes
                                  snake_case by default, e.g.
                                  publicationYear -> publication_year)

  Example from this project (Book.java):
    @Entity
    public class Book {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        ...
    }

  This single class definition is enough for Hibernate to create:
    CREATE TABLE book (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        title VARCHAR(255),
        ...
    );
  automatically, when the app starts (because of
  spring.jpa.hibernate.ddl-auto=update in application.properties).

4. RELATIONSHIPS - HOW TABLES CONNECT
------------------------------------------------------------------------
  This project models: ONE Author writes MANY Books.

  In Book.java (the "many" side - it holds the foreign key):
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

  In Author.java (the "one" side - it's just a view of the relationship,
  no actual foreign key column lives here):
    @OneToMany(mappedBy = "author")
    private List<Book> books;

  Rule of thumb: the side that has "Many" pointing FROM it (Book -> one
  Author) is the OWNING side and gets @JoinColumn. The other side just
  says mappedBy = "<field name on the owning side>".

  Other relationship types you'll encounter later:
    @OneToOne     - e.g. one User has one Profile
    @ManyToMany   - e.g. many Students enroll in many Courses (needs a
                    join table)

5. THE REPOSITORY INTERFACE - THE MAGIC PART
------------------------------------------------------------------------
  public interface BookRepository extends JpaRepository<Book, Long> {
  }

  That's it. No class, no implementation, no @Override methods. By
  extending JpaRepository<Book, Long> (Book = entity type, Long = type
  of its @Id field), you instantly get:
    save(book)          -> INSERT or UPDATE
    findById(id)         -> SELECT ... WHERE id = ?
    findAll()            -> SELECT * FROM book
    deleteById(id)        -> DELETE ... WHERE id = ?
    count()               -> SELECT COUNT(*)
  Spring scans this interface at startup and generates a real
  implementation behind the scenes using a dynamic proxy.

6. DERIVED QUERY METHODS - QUERIES FROM METHOD NAMES
------------------------------------------------------------------------
  You can add your own methods to the repository interface, and as long
  as the NAME follows Spring Data's keyword conventions, it generates
  the query automatically - still zero SQL written by you.

  findByTitle(String title)
    -> SELECT * FROM book WHERE title = ?

  findByAuthorNameContaining(String namePart)
    -> joins to Author through the "author" field, then "Name" reaches
       into Author.name, and "Containing" means SQL LIKE %...%
    -> SELECT b.* FROM book b JOIN author a ON b.author_id=a.id
       WHERE a.name LIKE %?%

  findByPublicationYearGreaterThan(int year)
    -> SELECT * FROM book WHERE publication_year > ?

  Common keywords: And, Or, Between, LessThan, GreaterThan, Like,
  Containing, OrderBy, IgnoreCase, In, IsNull, True/False.

7. JPQL - WHEN METHOD NAMES AREN'T ENOUGH
------------------------------------------------------------------------
  @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword%")
  List<Book> searchByTitleKeyword(@Param("keyword") String keyword);

  JPQL looks like SQL but is NOT SQL:
    - "FROM Book b"  refers to the ENTITY CLASS NAME "Book" (capital B,
      Java class), not the database table "book".
    - "b.title"      refers to the JAVA FIELD "title", not necessarily
      the literal column name.
  Hibernate translates this into real SQL against the actual table/
  column names at runtime.

8. APPLICATION.PROPERTIES - WHAT EACH LINE MEANS
------------------------------------------------------------------------
  spring.datasource.url=jdbc:h2:mem:librarydb
      -> connect to an IN-MEMORY H2 database named "librarydb".
         Data disappears when the app stops - perfect for learning/testing,
         not for production.

  spring.jpa.hibernate.ddl-auto=update
      -> Hibernate looks at your @Entity classes and creates/updates
         tables to match. Other values you'll see in real projects:
           none    - do nothing (production-safe, use migration tools)
           validate - check schema matches entities, fail if not
           create   - drop and recreate tables every restart (data lost)
           create-drop - like create, but also drops tables on shutdown

  spring.jpa.show-sql=true
      -> prints every SQL statement Hibernate generates to the console.
         Extremely useful while learning - watch the console as you hit
         your endpoints and see the real SQL being run.

  spring.h2.console.enabled=true
      -> turns on a web-based database browser at /h2-console so you can
         visually inspect tables/rows while the app is running.

9. EXECUTION STEPS
------------------------------------------------------------------------
  1. Unzip and import this folder's pom.xml into IntelliJ as a Maven
     project (right-click pom.xml -> Add as Maven Project).
  2. Wait for Maven to download dependencies.
  3. Open SpringDataJpaApplication.java -> right-click -> Run.
  4. Console should show Tomcat starting on port 8080, and Hibernate
     printing CREATE TABLE statements for "author" and "book" (because
     show-sql=true).
  5. Test endpoints with curl, Postman, or IntelliJ's HTTP client:

     Create an author:
       curl -X POST http://localhost:8080/api/authors \
         -H "Content-Type: application/json" \
         -d "{\"name\":\"J.K. Rowling\"}"
       -> note the "id" returned, e.g. 1

     Create a book linked to that author:
       curl -X POST http://localhost:8080/api/books \
         -H "Content-Type: application/json" \
         -d "{\"title\":\"Harry Potter\",\"isbn\":\"111\",\"publicationYear\":1997,\"author\":{\"id\":1}}"

     List all books:
       curl http://localhost:8080/api/books

     Try the derived query / JPQL endpoints:
       curl "http://localhost:8080/api/books/search/by-title?title=Harry%20Potter"
       curl "http://localhost:8080/api/books/search/by-author?name=Rowling"
       curl "http://localhost:8080/api/books/search/after-year?year=1990"
       curl "http://localhost:8080/api/books/search/jpql?keyword=Harry"

  6. Open http://localhost:8080/h2-console in a browser, use JDBC URL
     jdbc:h2:mem:librarydb, username sa, blank password, and click
     Connect to visually browse the "AUTHOR" and "BOOK" tables.
  7. Watch the Run console in IntelliJ each time you hit an endpoint -
     you'll see the exact SQL Hibernate generated for that call.

10. KEY TAKEAWAYS
------------------------------------------------------------------------
  - You almost never write raw SQL for basic CRUD; the repository
    interface does it for you.
  - Method NAMING is itself a query language for simple cases (derived
    queries).
  - @Query/JPQL is your escape hatch for anything complex, but it still
    operates on entity/field names, not raw table/column names.
  - Relationships are declared with annotations; Hibernate manages the
    foreign keys for you based on those annotations.
  - show-sql=true is your best learning tool - always keep it on while
    studying, turn it off in production (too noisy/slow).
========================================================================
