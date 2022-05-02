package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UbicacionDao {

    private final String urlDB = "jdbc:sqlite:./sqlite/db/proyecto.db";

    private Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(urlDB);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
