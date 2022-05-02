package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
    String deleteUbicacionRecienteMasAntigua();
    boolean clearUbicaciones();
    List<Ubicacion>getListaUbicaciones();
    HashSet<String> getListaUbicacionesActivas();
    HashSet<String> getListaUbicacionesRecientes();
    HashMap<String, ArrayList<String>> getListaAlias();

    HashSet<String> getListaServiciosActivos();
    HashMap<String,ArrayList<String>> getListaServiciosUbicacion();

    boolean addServicioActivo(String servicio);
    boolean deleteServicioActivo(String servicio);
    boolean clearServiciosActivos();

    boolean addServicioUbicacion(String servicio, String ubicacion);
    boolean deleteServicioUbicacion(String servicio, String ubicacion);

}
