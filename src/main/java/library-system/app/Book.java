package librarysystem.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private int authorId;

    public Book(String title, int authorId) {
        this.title = title;
        this.authorId = authorId;
    }

    @Override 
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", authorId=" + authorId +
            '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public static void createTables() {
        String createAuthorsTableSQL = "CREATE TABLE IF NOT EXISTS authors ("+
                                       "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                       "name TEXT NOT NULL);";

        String createBooksTableSQL = "CREATE TABLE IF NOT EXISTS books ("+
                                     "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                     "title TEXT NOT NULL, "+
                                     "author_id INTEGER NOT NULL, "+
                                     "FOREIGN KEY (author_id) REFERENCES authors(id));";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt1 = connection.prepareStatement(createAuthorsTableSQL);
             PreparedStatement pstmt2 = connection.prepareStatement(createBooksTableSQL)) {

            pstmt1.executeUpdate();
            pstmt2.executeUpdate();
            System.out.println("Tables 'authors' and 'books' created successfully.");

        } catch (SQLException e) {
            System.err.println("Erro ao criar as tables.");
            e.printStackTrace();
        }
    }

    public static void insertBooks(List<Book> books) {
        String sqlStr = "INSERT INTO books (title, author_id) VALUES (?, ?);";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            for (Book book : books) {
                pstmt.setString(1, book.getTitle());
                pstmt.setInt(2, book.getAuthorId());
                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir diversos livros.");
            e.printStackTrace();
        }
    }

    public static List<Book> getByTitle(String searchString) {
        String sqlStr = "SELECT * FROM books WHERE title LIKE ?;";

        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            pstmt.setString(1, "%" + searchString + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int authorId = rs.getInt("author_id");
                Book book = new Book(title, authorId);
                book.setId(id);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao puxar livros .");
            e.printStackTrace();
        }

        return books;
    }

    public static void listBooks() {
        String sqlStr = "SELECT b.title, a.name FROM books b " +
                        "JOIN authors a ON b.author_id = a.id;";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("title") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar livros.");
            e.printStackTrace();
        }
    }

    public void update() {
        String sqlStr = "UPDATE books SET title = ?, author_id = ? WHERE id = ?;";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            pstmt.setString(1, this.title);
            pstmt.setInt(2, this.authorId);
            pstmt.setInt(3, this.id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar");
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        String sqlStr = "DELETE FROM books WHERE id = ?;";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Livro: " + id + " excluido.");
            } else {
                System.out.println("nenhum livro encontrado." );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao excluir.");
            e.printStackTrace();
        }
    }
}
