package modelo;

import java.util.Map;

public interface IntGestorGeocoding {
    static Map<String, Object> jsonToMap(String str) {
        return null;
    }

    Ubicacion peticion(String toponimo);
}
