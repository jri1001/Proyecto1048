package modelo;

import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class GestorSQLite implements IntGestorSQLite{
    private static GestorSQLite gestorSQLite;
    private String urlDB = "jdbc:sqlite:./sqlite/db/proyecto.db"; //default relative path to database

    public GestorSQLite(){}

    public static GestorSQLite getGestorSQLite(){
        if (gestorSQLite==null){
            gestorSQLite=new GestorSQLite();
        }
        return gestorSQLite;
    }

    private boolean execute(String sql, String[] parametros){ //Usar si no se requiere un ResultSet. De esta manera se evita inyección de SQL en parametros.
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for(int i=0; i<parametros.length;i++){
                pstmt.setString(i+1, parametros[i]);
            }
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean cambiarDB(String urlDB){ //para conectarse a DB sin eliminar sus datos
        return conectarDB(urlDB);
    }

    public boolean crearDB(String nombre){
        if(conectarDB(nombre)){ //conectarse a una DB que no existe la crea
            try (Connection conn = DriverManager.getConnection(this.urlDB);
                 Statement stmt = conn.createStatement()) {

                //create tables
                String sql= "DROP TABLE IF EXISTS Ubicacion;";
                stmt.execute(sql);
                sql="CREATE TABLE Ubicacion( nombre Varchar(50) PRIMARY KEY, ciudad Varchar(30) NOT NULL, provincia Varchar(30) NOT NULL, cod_postal Varchar(30) NOT NULL, longitud Varchar(30) NOT NULL, latitud Varchar(30) NOT NULL, UNIQUE(longitud, latitud));";
                stmt.execute(sql);

                sql= "DROP TABLE IF EXISTS Alias;";
                stmt.execute(sql);
                sql="CREATE TABLE Alias( nombre Varchar(50) NOT NULL, alias Varchar(50) UNIQUE, PRIMARY KEY (nombre, alias), FOREIGN KEY (nombre) REFERENCES Ubicacion(nombre) ON DELETE CASCADE ON UPDATE CASCADE);";
                stmt.execute(sql);

                sql= "DROP TABLE IF EXISTS UbicacionesActivas;";
                stmt.execute(sql);
                sql="CREATE TABLE UbicacionesActivas( nombre Varchar(50) PRIMARY KEY, FOREIGN KEY (nombre) REFERENCES Ubicacion(nombre) ON DELETE CASCADE ON UPDATE CASCADE);";
                stmt.execute(sql);

                sql= "DROP TABLE IF EXISTS UbicacionesRecientes;";
                stmt.execute(sql);
                sql="CREATE TABLE UbicacionesRecientes( nombre Varchar(50) PRIMARY KEY, fecha DATETIME, FOREIGN KEY (nombre) REFERENCES Ubicacion(nombre) ON DELETE CASCADE ON UPDATE CASCADE);";
                stmt.execute(sql);

                sql= "DROP TABLE IF EXISTS MisUbicaciones;";
                stmt.execute(sql);
                sql="CREATE TABLE MisUbicaciones( nombre Varchar(50) PRIMARY KEY, fecha DATETIME, FOREIGN KEY (nombre) REFERENCES Ubicacion(nombre) ON DELETE CASCADE ON UPDATE CASCADE);";
                stmt.execute(sql);

                sql= "DROP TABLE IF EXISTS ServiciosActivos;";
                stmt.execute(sql);
                sql="CREATE TABLE ServiciosActivos( nombre Varchar(50) PRIMARY KEY);";
                stmt.execute(sql);

                sql= "DROP TABLE IF EXISTS ServiciosUbicacion;";
                stmt.execute(sql);
                sql="CREATE TABLE ServiciosUbicacion( nombre Varchar(50) NOT NULL, ubicacion Varchar(50) NOT NULL, PRIMARY KEY(nombre, ubicacion), FOREIGN KEY (nombre) REFERENCES ServiciosActivos(nombre) ON DELETE CASCADE ON UPDATE CASCADE, FOREIGN KEY (ubicacion) REFERENCES Ubicacion(nombre) ON DELETE CASCADE ON UPDATE CASCADE);";
                stmt.execute(sql);
                return true;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public boolean copyDB(String myUrl) {
        String sql= "ATTACH DATABASE ? AS other;";
        execute(sql,new String[]{myUrl});

        sql= "DETACH ?;";
        return execute(sql,new String[]{myUrl});
    }

    public boolean conectarDB(String nombre){
        this.urlDB="jdbc:sqlite:./sqlite/db/"+nombre;
        return comprobarConexion();
    }

    public Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            conn = DriverManager.getConnection(urlDB,config.toProperties());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private boolean comprobarConexion(){
        return connect() != null;
    }

    public boolean addUbicacion(Ubicacion ubicacion){
        String sql ="INSERT INTO Ubicacion VALUES(?, ?, ?, ?, ?, ?);";
        return execute(sql,new String[]{ubicacion.getNombre(),ubicacion.getCiudad(),ubicacion.getProvincia(),ubicacion.getCod_postal(),ubicacion.getLongitud(),ubicacion.getLatitud()});
    }

    public boolean addUbicacionReciente(String toponimo){
        String sql ="INSERT INTO UbicacionesActivas VALUES(?, ?);";
        return execute(sql,new String[]{formatearToponimo(toponimo),"CURRENT_DATE"});
    }

    public boolean activarUbicacion(String toponimo){
        String sql ="INSERT INTO UbicacionesActivas VALUES(?);";
        return execute(sql,new String[]{formatearToponimo(toponimo)});
    }

    public boolean addAlias(String nombre, String alias) {
        String sql ="INSERT INTO Alias VALUES(?,?);";
        return execute(sql,new String[]{nombre,alias});
    }

    public boolean desactivarUbicacion(String toponimo){
        String sql ="DELETE FROM UbicacionesActivas WHERE nombre=?;";
        return execute(sql,new String[]{formatearToponimo(toponimo)});
    }

    public boolean deleteAlias(String nombre, String alias) {
        String sql ="DELETE FROM Alias WHERE nombre=? AND alias=?;";
        return execute(sql,new String[]{nombre,alias});
    }

    public Ubicacion getUbicacion(String toponimo){
        String sql ="SELECT * from Ubicacion WHERE nombre="+formatearToponimo(toponimo)+";";
        Ubicacion ubicacion=null;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            ubicacion=new Ubicacion();
            ubicacion.setNombre(rs.getString("nombre"));
            ubicacion.setCiudad(rs.getString("ciudad"));
            ubicacion.setCod_postal(rs.getString("cod_postal"));
            ubicacion.setProvincia(rs.getString("provincia"));
            ubicacion.setLongitud(rs.getString("longitud"));
            ubicacion.setLatitud(rs.getString("latitud"));


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ubicacion;
    }

    public Ubicacion getUbicacion(String[] coordenadas){
        String sql ="SELECT * from Ubicacion WHERE longitud="+coordenadas[1]+" AND latitud="+coordenadas[0]+";"; //vulnerable a inyeccion de sql
        Ubicacion ubicacion=null;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            ubicacion=new Ubicacion();
            ubicacion.setNombre(rs.getString("nombre"));
            ubicacion.setCiudad(rs.getString("ciudad"));
            ubicacion.setCod_postal(rs.getString("cod_postal"));
            ubicacion.setProvincia(rs.getString("provincia"));
            ubicacion.setLongitud(rs.getString("longitud"));
            ubicacion.setLatitud(rs.getString("latitud"));


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ubicacion;
    }

    public boolean deleteUbicacion(String toponimo){
        String sql="DELETE FROM Ubicacion WHERE nombre=?;";
        return execute(sql,new String[]{formatearToponimo(toponimo)});
    }

    public boolean deleteUbicacionReciente(String toponimo) {
        String sql="DELETE FROM UbicacionesRecientes WHERE nombre=?;";
        return execute(sql,new String[]{formatearToponimo(toponimo)});
    }

    public boolean addUbicacionPrevia(String toponimo) {
        LocalDate fecha = LocalDate.now();
        String sql="INSERT INTO UbicacionesRecientes VALUES(?,fecha);";
        return execute(sql,new String[]{formatearToponimo(toponimo)});
    }

    public String deleteUbicacionRecienteMasAntigua() {
        String sql="SELECT nombre FROM UbicacionesRecientes WHERE fecha=(SELECT MIN(fecha) FROM UbicacionesRecientes);";
        String toponimo=null;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            toponimo=formatearToponimo(rs.getString("nombre"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if(deleteUbicacionReciente(toponimo)){
            return formatearToponimo(toponimo);
        }
        return null;
    }

    public boolean clearUbicaciones(){
        return execute("DELETE FROM Ubicacion;",new String[]{});
    }

    public List<Ubicacion> getListaUbicaciones(){
        String sql ="SELECT * FROM Ubicacion;";
        ArrayList<Ubicacion>listaUbicaciones=new ArrayList<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()){
                Ubicacion ubicacion=new Ubicacion();
                ubicacion.setNombre(rs.getString("nombre"));
                ubicacion.setCiudad(rs.getString("ciudad"));
                ubicacion.setCod_postal(rs.getString("cod_postal"));
                ubicacion.setProvincia(rs.getString("provincia"));
                ubicacion.setLongitud(rs.getString("longitud"));
                ubicacion.setLatitud(rs.getString("latitud"));
                listaUbicaciones.add(ubicacion);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listaUbicaciones;
    }

    public HashSet<String> getListaUbicacionesActivas(){
        String sql ="SELECT * FROM UbicacionesActivas;";
        HashSet<String> listaUbicacionesActivas=new HashSet<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()){
                listaUbicacionesActivas.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listaUbicacionesActivas;
    }

    public HashSet<String> getListaUbicacionesRecientes() {
        String sql ="SELECT * FROM UbicacionesRecientes;";
        HashSet<String> listaUbicacionesRecientes=new HashSet<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()){
                listaUbicacionesRecientes.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listaUbicacionesRecientes;
    }

    public HashSet<String> getUbicacionesFavoritas() {
        String sql ="SELECT * FROM MisUbicaciones;";
        HashSet<String> listaUbicaciones=new HashSet<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()){
                listaUbicaciones.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listaUbicaciones;
    }

    public HashMap<String, ArrayList<String>> getListaAlias() {
        String sql ="SELECT * FROM Alias;";
        HashMap<String,ArrayList<String>> mapaAlias=new HashMap<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()){
                String nombre=rs.getString("nombre");
                ArrayList<String> lista=new ArrayList<String>();
                if(mapaAlias.containsKey(nombre)){
                    lista= mapaAlias.get(nombre);
                } else{
                    lista=new ArrayList<>();
                }
                lista.add(rs.getString("alias"));
                mapaAlias.put(nombre,lista);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return mapaAlias;
    }

    public HashSet<String> getListaServiciosActivos() {
        String sql ="SELECT * FROM ServiciosActivos;";
        HashSet<String> listaServiciosActivos=new HashSet<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()){
                listaServiciosActivos.add(rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listaServiciosActivos;
    }

    public HashMap<String, ArrayList<String>> getListaServiciosUbicacion() {
        String sql ="SELECT * FROM ServiciosUbicacion;";
        HashMap<String,ArrayList<String>> mapaServicio=new HashMap<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()){
                String nombre=rs.getString("ubicacion");
                ArrayList<String> lista=new ArrayList<String>();
                if(mapaServicio.containsKey(nombre)){
                    lista= mapaServicio.get(nombre);
                } else{
                    lista=new ArrayList<>();
                }
                lista.add(rs.getString("nombre"));
                mapaServicio.put(nombre,lista);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return mapaServicio;
    }

    public boolean addServicioActivo(String servicio) {
        String sql="INSERT INTO ServiciosActivos VALUES(?);";
        return execute(sql,new String[]{servicio});
    }

    public boolean deleteServicioActivo(String servicio) {
        String sql="DELETE FROM ServiciosActivos WHERE nombre=?;";
        return execute(sql,new String[]{servicio});
    }

    public boolean clearServiciosActivos() {
        String sql="DELETE FROM ServiciosActivos;";
        return execute(sql,new String[]{});
    }

    public boolean addServicioUbicacion(String servicio, String ubicacion) {
        String sql="INSERT INTO ServiciosUbicacion VALUES(?, ?);";
        return execute(sql,new String[]{servicio,ubicacion});
    }

    public boolean deleteServicioUbicacion(String servicio, String ubicacion) {
        String sql="DELETE FROM ServiciosUbicacion WHERE nombre=? AND ubicacion=?;";
        return execute(sql,new String[]{servicio,ubicacion});
    }
    public String formatearToponimo(String toponimo){
        return toponimo.toLowerCase(Locale.ROOT); //todos los toponimos en minuscula
    }
}
