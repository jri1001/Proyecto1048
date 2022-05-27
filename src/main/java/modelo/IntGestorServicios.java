package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface IntGestorServicios {

    List<String> getListaServicios();
    List<String> getListaServiciosActivos();
    List<String> getListaServiciosUbicacion(String nombre);

    void syncDB(HashSet<String> listaServiciosActivos, HashMap<String, ArrayList<String>> listaServiciosUbicacion);

    HashMap<String,ArrayList<HashMap<String,String>>> peticion(String nombreUbicacion);
    HashMap<String,ArrayList<HashMap<String,String>>> consulta(String nombreUbicacion);

    boolean clearServicios();
    boolean clearServiciosActivos();
    HashSet<String> ServiciosActivos();

    boolean addServicio(String nombre, Servicio servicio);
    boolean deleteServicio(String servicio);

    boolean activarServicio(String servicio);
    boolean desactivarServicio(String servicio);

    boolean addServicioUbicacion(String servicio, String ubicacion);
    boolean deleteServicioUbicacion(String servicio, String ubicacion);

    HashMap<String,String > infoServicios();
}
