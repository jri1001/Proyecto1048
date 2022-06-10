package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public interface IntGestorSQLite {

    boolean conectarDB(String urlDB);
    boolean cambiarDB(String urlDB);
    boolean crearDB(String nombre);
    boolean copyDB(String myUrl);

    boolean addUbicacion(Ubicacion ubicacion);
    boolean addUbicacionReciente(String toponimo);
    boolean activarUbicacion(String toponimo);
    boolean addAlias(String nombre, String alias);
    boolean desactivarUbicacion(String toponimo);
    boolean deleteAlias(String nombre, String alias);
    Ubicacion getUbicacion(String toponimo);
    Ubicacion getUbicacion(String[] coordenadas);
    boolean deleteUbicacion(String toponimo);
    boolean deleteUbicacionReciente(String toponimo);
    boolean addUbicacionPrevia(String toponimo,String fecha);
    boolean addUbicacionFavorita(String toponimo, String fecha);
    boolean deleteUbicacionFavorita(String toponimo);
    String deleteUbicacionRecienteMasAntigua();
    boolean clearUbicaciones();
    List<Ubicacion>getListaUbicaciones();
    List<String> getListaUbicacionesNombre();
    HashSet<String> getListaUbicacionesActivas();
    HashSet<String> getListaUbicacionesRecientes();
    HashMap<String, ArrayList<String>> getListaAlias();

    HashSet<String> getListaServiciosActivos();
    HashMap<String,ArrayList<String>> getListaServiciosUbicacion();
    String UbicacionGrupo(String grupo, String nombre);
    boolean addUbicacionGrupo(String nombre,String grupo);
    boolean clearGrupoUbicaciones(String grupo);
    ArrayList<String> getListaGruposUbicaciones();
    HashSet<String> getGrupos();

    boolean addServicioActivo(String servicio);
    boolean deleteServicioActivo(String servicio);
    boolean clearServiciosActivos();

    boolean addServicioUbicacion(String servicio, String ubicacion);
    boolean deleteServicioUbicacion(String servicio, String ubicacion);

}
