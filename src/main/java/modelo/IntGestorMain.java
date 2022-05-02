package modelo;

import java.util.List;
import java.util.Map;

public interface IntGestorMain extends IntGestorServicios{ //todo:creo que esta es innecesaria
    void inject(GestorGeocoding gestorGeocoding);
    void inject(GestorUbicacion gestorUbicacion);
    void inject(GestorServicios gestorServicios);
    void inject(GestorSQLite gestorSQLite);
    Map<String, Ubicacion> getListaUbicaciones();
    boolean verificarGestor(String gestor);
    Ubicacion addUbicacion(String toponimo);
    Ubicacion addUbicacion(String[] coordenadas);
    Ubicacion getUbicacion(String toponimo);
    Ubicacion getUbicacion(String[] coordenadas);
    String getCoordenadas(String toponimo);
    List<String> getListaAlias();
    boolean asignarAlias(String alias, String toponimo);
    boolean clearUbicaciones();
    public void syncDB();

}
