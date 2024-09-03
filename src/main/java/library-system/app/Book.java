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
    private String author;


    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    @Override 
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", author='" + author + '\'' +
            '}';
    }

    // Getters and Setters

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // Database Operations

    public static void createTable() {
        String sqlStr = "CREATE TABLE IF NOT EXISTS books ("+
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        "title TEXT NOT NULL, "+
                        "author TEXT NOT NULL);";

        try (Connection connection = DatabaseHelper.connect();
            PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {
        
            pstmt.executeUpdate();
            System.out.println("Table 'books' created successfully.");

        } catch (SQLException e) {
            System.err.println("Error creating table.");
            e.printStackTrace();
        }
    }

    public static void insert(List<Book> books) {
        String sqlStr = "INSERT INTO books (title, author) VALUES (?, ?);";
        
        try (Connection connection = DatabaseHelper.connect();
            PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {
            
            for (Book book : books) {
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());
                pstmt.addBatch();
            }
            
            pstmt.executeBatch();
        } catch (SQLException e) {
            System.err.println("Error inserting multiple books into database.");
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
                String author = rs.getString("author");
                Book book = new Book(title, author);
                book.setId(id);
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println("Error getting books from database.");
            e.printStackTrace();
        }

        return books;
    }

    public static void listBooks() {
        String sqlStr = "SELECT * FROM books;";

        try (Connection connection = DatabaseHelper.connect();
            PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("title") + " - " + rs.getString("author")); 
            }
        } catch (SQLException e) {
            System.err.println("Error getting books from database");
            e.printStackTrace();
        }
    }

    public void update() {
        String sqlStr = "UPDATE books SET title = ?, author = ? WHERE id = ?;";
     
        try (Connection connection = DatabaseHelper.connect();
            PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {


            pstmt.setString(1, this.title);
            pstmt.setString(2, this.author);
            pstmt.setInt(3, this.id);

            pstmt.executeUpdate();
        } catch(SQLException e) {
            System.err.println("Error updating the book");
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
                System.out.println("Book with ID: " + id + " deleted");
            } else {
                System.out.println("No book found with ID " + id);
            }
            
            System.out.println(rowsAffected + " rows affected");
        } catch (SQLException e) {
            System.err.println("Error deleting book from database.");
            e.printStackTrace();
        }
    }
}
