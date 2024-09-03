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

    @Override 
    public String toString() {
        return "Author{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    // Getters and Setters

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
}
