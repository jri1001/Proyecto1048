package modelo;

import java.util.*;

public interface IntGestorUbicacion {
    void syncDB(List<Ubicacion> lista, HashSet<String> activas, HashSet<String> recientes, HashMap<String, ArrayList<String>> alias);
    void addUbicacion(Ubicacion ubicacion);
    boolean addUbicacionReciente(String toponimo);
    boolean deleteUbicacion(String toponimo);
    Map<String, Ubicacion> getListaUbicacion();
    List<String> getListaUbicacionesActivas();
    HashSet<String> getListaUbicacionesRecientes();
    boolean activarUbicacion(String toponimo);
    boolean desactivarUbicacion(String toponimo);
    boolean deleteUbicacionReciente(String toponimo);
    Ubicacion getUbicacion(String toponimo);
    boolean contieneUbicacion(String toponimo);
    boolean contieneUbicacionActiva(String toponimo);
    boolean addAlias(String nombre, String alias);
    ArrayList<String> getListaAlias(String nombre);
    boolean deleteAlias(String nombre, String alias);
    boolean clear();
}
