package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Servicio {
    String nombre;
    String info;

    public String getNombre() {
        return nombre;
    }

    public String getInfo() {
        return info;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    abstract ArrayList<HashMap<String, String>> peticion(String location);
}

