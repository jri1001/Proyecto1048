import java.sql.*;

public class Database { //todo: meterlo en el proceso de recuperacion de datos
    public static void createNewDatabase() {

        String url = "jdbc:sqlite:./sqlite/db/proyecto.db"; //relative path

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable() {
        String url = "jdbc:sqlite:./sqlite/db/proyecto.db"; //relative path
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            //drop table
            String sql= "DROP TABLE Ubicacion";
            stmt.execute(sql);
            System.out.println("Borrada tabla ubicacion");
            // create a new table
            sql= "CREATE TABLE IF NOT EXISTS Ubicacion(nombre Varchar(50) PRIMARY KEY, ciudad Varchar(30) NOT NULL, provincia Varchar(30) NOT NULL, cod_postal Varchar(30) NOT NULL, longitud Varchar(30) NOT NULL, latitud Varchar(30) NOT NULL, alias Varchar(30) NULL, UNIQUE(longitud, latitud) );";
            stmt.execute(sql);
            System.out.println("Creada tabla ubicacion");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        createNewDatabase();
        createNewTable();
    }
}
