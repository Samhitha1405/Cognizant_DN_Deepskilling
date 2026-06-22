import java.util.Arrays;
import java.util.Comparator;

class Book {
    int bookId;
    String title;
    String author;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
    }

    @Override
    public String toString() {
        return "ID:" + bookId + " \"" + title + "\" by " + author;
    }
}

public class LibraryManagement {

    // O(n) - works regardless of order, but checks every book in the worst case
    static Book linearSearchByTitle(Book[] books, String title) {
        for (Book b : books) {
            if (b.title.equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

    // O(log n) - requires books[] to already be sorted by title
    static Book binarySearchByTitle(Book[] sortedBooks, String title) {
        int low = 0, high = sortedBooks.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = sortedBooks[mid].title.compareToIgnoreCase(title);
            if (cmp == 0) return sortedBooks[mid];
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }

    public static void main(String[] args) {
        Book[] catalog = {
                new Book(1, "The Hobbit", "J.R.R. Tolkien"),
                new Book(2, "Dune", "Frank Herbert"),
                new Book(3, "1984", "George Orwell"),
                new Book(4, "Foundation", "Isaac Asimov"),
                new Book(5, "Neuromancer", "William Gibson")
        };

        System.out.println("=== Linear Search ===");
        Book result = linearSearchByTitle(catalog, "1984");
        System.out.println("Search '1984' -> " + (result != null ? result : "Not found"));

        Book[] sortedCatalog = catalog.clone();
        Arrays.sort(sortedCatalog, Comparator.comparing(b -> b.title.toLowerCase()));

        System.out.println("\nCatalog sorted by title (needed for binary search):");
        for (Book b : sortedCatalog) System.out.println("  " + b);

        System.out.println("\n=== Binary Search ===");
        result = binarySearchByTitle(sortedCatalog, "1984");
        System.out.println("Search '1984' -> " + (result != null ? result : "Not found"));

        System.out.println("\n=== Searching for a title that doesn't exist ===");
        System.out.println("Linear: " + linearSearchByTitle(catalog, "Brave New World"));
        System.out.println("Binary: " + binarySearchByTitle(sortedCatalog, "Brave New World"));
    }
}