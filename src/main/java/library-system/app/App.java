package librarysystem.app;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // Step 1: Create the 'books' table
        Book.createTable();

        // Step 2: Insert books into the table
        Book book1 = new Book("1984", "George Orwell");
        Book book2 = new Book("Brave New World", "Aldous Huxley");
        Book book3 = new Book("Fahrenheit 451", "Ray Bradbury");

        List<Book> books = Arrays.asList(book1, book2, book3);
        Book.insert(books);

        // Step 3: List all books
        System.out.println("List of all books:");
        Book.listBooks();

        // Step 4: Search for books with 'New' in the title
        System.out.println("\nSearch for books with 'New' in the title:");
        List<Book> searchResults = Book.getByTitle("New");
        for (Book book : searchResults) {
            System.out.println(book);
        }

        // Step 5: Update a book's information
        book2.setTitle("A Brave New World");
        book2.update();

        // List books again to see the update
        System.out.println("\nList of all books after update:");
        Book.listBooks();

        // Step 6: Delete a book by ID
        System.out.println("\nDeleting book with ID 1...");
        Book.delete(1);

        // List books again to see the deletion
        System.out.println("\nList of all books after deletion:");
        Book.listBooks();
    }
}
