package librarysystem.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Author {
    private int id;
    private String name;

    
    public Author(String name) {
        this.name = name;
    }

    
    public Author(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void createTable() {
        String sqlStr = "CREATE TABLE IF NOT EXISTS authors (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL);";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            pstmt.executeUpdate();
            System.out.println("Table 'authors' Criada.");

        } catch (SQLException e) {
            System.err.println("Erro ao criar a table.");
            e.printStackTrace();
        }
    }

    public static void insert(List<Author> authors) {
        String sqlStr = "INSERT INTO authors (name) VALUES (?);";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS)) {

            for (Author author : authors) {
                pstmt.setString(1, author.getName());
                pstmt.addBatch();
            }

            pstmt.executeBatch();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            int index = 0;
            while (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                authors.get(index++).setId(generatedId);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir autores.");
            e.printStackTrace();
        }
    }

    public static List<Author> getByName(String searchString) {
        String sqlStr = "SELECT * FROM authors WHERE name LIKE ?;";

        List<Author> authors = new ArrayList<>();

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            pstmt.setString(1, "%" + searchString + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Author author = new Author(id, name); // Use o novo construtor
                authors.add(author);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao puxar autores");
            e.printStackTrace();
        }

        return authors;
    }

    public static void listAuthors() {
        String sqlStr = "SELECT * FROM authors;";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Erro de puxar autores.");
            e.printStackTrace();
        }
    }

    public void update() {
        String sqlStr = "UPDATE authors SET name = ? WHERE id = ?;";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            pstmt.setString(1, this.name);
            pstmt.setInt(2, this.id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro na atualizacao.");
            e.printStackTrace();
        }
    }

    public static void delete(int id) {
        String sqlStr = "DELETE FROM authors WHERE id = ?;";

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement pstmt = connection.prepareStatement(sqlStr)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("autor do ID: " + id + " Deletado.");
            } else {
                System.out.println("Nenhum autor encontrado.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar autor");
            e.printStackTrace();
        }
    }
}
