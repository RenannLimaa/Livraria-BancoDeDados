package librarysystem.app;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        Book.createTables();

        Author author1 = new Author("George Orwell");
        Author author2 = new Author("Aldous Huxley");
        Author author3 = new Author("Ray Bradbury");

        List<Author> authors = Arrays.asList(author1, author2, author3);
        Author.insert(authors);

        int author1Id = Author.getByName("George Orwell").get(0).getId();
        int author2Id = Author.getByName("Aldous Huxley").get(0).getId();
        int author3Id = Author.getByName("Ray Bradbury").get(0).getId();

        Book book1 = new Book("1984", author1Id);
        Book book2 = new Book("Brave New World", author2Id);
        Book book3 = new Book("New Fahrenheit 451", author3Id);

        List<Book> books = Arrays.asList(book1, book2, book3);
        Book.insertBooks(books);

        System.out.println("Liste todos os livros:");
        Book.listBooks();

        System.out.println("\nProcure por livros com 'New' no titulo:");
        List<Book> searchResults = Book.getByTitle("New");
        for (Book book : searchResults) {
            System.out.println(book);
        }

        book2.setTitle("A Brave New World");
        book2.update();

        System.out.println("\nListe todos os livros após a atualização:");
        Book.listBooks();

        System.out.println("\nDeletando o livro com ID 1...");
        Book.delete(1);

        System.out.println("\nLista de todos os livros apos deletar:");
        Book.listBooks();

        System.out.println("\nLista de todos os autores:");
        Author.listAuthors();

        System.out.println("\nProcure por livros com 'Huxley' no titulo:");
        List<Author> authorSearchResults = Author.getByName("Huxley");
        for (Author author : authorSearchResults) {
            System.out.println(author);
        }

        author2.setName("Aldous Leonard Huxley");
        author2.update();

        System.out.println("\nLista de todos os autores apos a atualizacao:");
        Author.listAuthors();

        System.out.println("\nDeletando o autor com o ID 1...");
        Author.delete(1);

        System.out.println("\nLista de todos os autores apos deletar:");
        Author.listAuthors();
    }
}
