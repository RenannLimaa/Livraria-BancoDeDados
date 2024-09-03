package librarysystem.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {
    private static final String url = "jdbc:sqlite:base.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
