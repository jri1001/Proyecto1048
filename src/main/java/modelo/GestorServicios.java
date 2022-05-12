package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GestorServicios implements IntGestorServicios {
    private HashMap<String,Servicio> listaServicios;
    private HashSet<String> listaServiciosActivos;
    private HashMap<String,ArrayList<String>> listaServiciosUbicacion;

    private static GestorServicios gestorServicios;

    public GestorServicios() {
        this.listaServicios = new HashMap<>();
        listaServicios.put("TicketMaster",GestorTicketMaster.getGestorTicketmaster());
        listaServicios.put("OpenWeather",GestorOpenWeather.getGestorOpenWeather());
        listaServicios.put("NewsDataIO",GestorNewsDataIO.getGestorNewsDataIO());
        this.listaServiciosActivos = new HashSet<>();
        this.listaServiciosUbicacion=new HashMap<>();

    }
    public static GestorServicios getGestorServicios(){
        if (gestorServicios==null){
            gestorServicios=new GestorServicios();
        }
        return gestorServicios;
    }

    public List<String> getListaServicios() {
        return new ArrayList<>(listaServicios.keySet());
    }
    public List<String> getListaServiciosActivos() {
        return new ArrayList<>(listaServiciosActivos);
    }

    public List<String> getListaServiciosUbicacion(String nombre) {
        List<String> lista=listaServiciosUbicacion.get(nombre);
        if(lista==null){
            lista=new ArrayList<>();
        }
        return lista;
    }

    public void syncDB(HashSet<String> listaServiciosActivos, HashMap<String, ArrayList<String>> listaServiciosUbicacion) {
        this.listaServiciosActivos=listaServiciosActivos;
        this.listaServiciosUbicacion=listaServiciosUbicacion;
    }

    public HashMap<String,ArrayList<HashMap<String,String>>> peticion(String nombreUbicacion) {
        HashMap<String,ArrayList<HashMap<String,String>>> mapa=new HashMap<>();
        for(String servicio: getListaServiciosUbicacion(nombreUbicacion)){
            mapa.put(servicio,listaServicios.get(servicio).peticion(nombreUbicacion));
        }
        return mapa;
    }

    public HashMap<String,ArrayList<HashMap<String,String>>> consulta(String nombreUbicacion) {
        HashMap<String,ArrayList<HashMap<String,String>>> mapa=new HashMap<>();
        for(String servicio: listaServiciosActivos){
            mapa.put(servicio,listaServicios.get(servicio).peticion(nombreUbicacion));
        }
        return mapa;
    }

    public boolean addServicio(String nombre, Servicio servicio){
        return listaServicios.put(nombre, servicio)!=null;
    }

    public boolean deleteServicio(String nombre){
        if(listaServicios.containsKey(nombre)){
            desactivarServicio(nombre);
            return listaServicios.remove(nombre)!=null;
        }
        return false;
    }

    public boolean clearServicios(){
        listaServicios.clear();
        listaServiciosActivos.clear();
        return listaServicios.isEmpty() && listaServiciosActivos.isEmpty();
    }

    public boolean clearServiciosActivos(){
        listaServiciosActivos.clear();
        return listaServiciosActivos.isEmpty();
    }

    public boolean activarServicio(String servicio){
        if(listaServicios.get(servicio)!=null){
            return listaServiciosActivos.add(servicio);
        }
        return false;
    }

    public boolean desactivarServicio(String nombre){
        if(listaServicios.containsKey(nombre) && listaServiciosActivos.remove(nombre)){
            for(String key:listaServiciosUbicacion.keySet()){
                ArrayList<String> lista=listaServiciosUbicacion.get(key);
                if(lista.contains(nombre)){
                    lista.remove(nombre);
                    listaServiciosUbicacion.put(key,lista);
                }
            }
            return true;
        }
        return false;
    }

    public boolean addServicioUbicacion(String servicio, String ubicacion) {
        ArrayList<String> lista=listaServiciosUbicacion.get(ubicacion);
        if(lista==null){
            lista=new ArrayList<>();
        }
        if(lista.add(servicio)){
            listaServiciosUbicacion.put(ubicacion,lista);
            return true;
        }
        return false;
    }

    public boolean deleteServicioUbicacion(String servicio, String ubicacion) {
        ArrayList<String> lista=listaServiciosUbicacion.get(ubicacion);
        if(lista.remove(servicio)){
            listaServiciosUbicacion.put(ubicacion,lista);
            return true;
        }
        return false;
    }

    public HashMap<String,String > infoServicios(){
        HashMap<String,String> mapa= new HashMap<>();
        mapa.put("TicketMaster","Proporciona información de eventos relacionados con la ubicación seleccionada. Actualizado diariamente. Para mas información consultar https://www.ticketmaster.es/");
        mapa.put("OpenWeather","Proporciona información del clima de la ubicación seleccionada, incluyendo temperatura, humedad y previsión meteorológica. Actualizado diariamente. Para mas información consultar https://openweathermap.org/");
        mapa.put("NewsDataIO","Proporciona información de noticias relacionadas con la ubicación seleccionada, tanto actual como histórica. Actualizado diariamente. Para mas información consultar https://newsdata.io/");
        return mapa;
    }
}