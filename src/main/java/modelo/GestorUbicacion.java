package modelo;

import java.util.*;

public class GestorUbicacion implements IntGestorUbicacion {

    //Listas de ubicaciones en el sistema -> <Toponimo,Coordenadas>
    private Map<String, Ubicacion> listaUbicaciones;
    private HashSet<String> listaUbicacionesActivas; //set con toponimos
    private HashSet<String> listaUbicacionesRecientes;
    private Map<String, ArrayList<String>> listaAlias; //mapa con [toponimo=key, lista de aliases]
    private static GestorUbicacion gestorUbicacion;

    public GestorUbicacion(){
        this.listaUbicaciones = new HashMap<>();
        this.listaUbicacionesActivas = new HashSet<>();
        this.listaUbicacionesRecientes = new HashSet<>();
        this.listaAlias = new HashMap<>();
    }

    public static GestorUbicacion getGestorUbicacion(){
        if (gestorUbicacion==null){
            gestorUbicacion=new GestorUbicacion();
        }
        return gestorUbicacion;
    }

    public void syncDB(List<Ubicacion> lista, HashSet<String> activas, HashSet<String> recientes, HashMap<String, ArrayList<String>> alias){
        listaUbicaciones.clear();
        for(Ubicacion ubicacion:lista){
            listaUbicaciones.put(ubicacion.getNombre(), ubicacion);
        }
        listaUbicacionesActivas=activas;
        listaUbicacionesRecientes=recientes;
        listaAlias=alias;
    }

    //Metodos de la clase Gestor Ubicacion
    public Map<String, Ubicacion> getListaUbicacion(){
        return new HashMap<>(listaUbicaciones);
    }

    public List<String> getListaUbicacionesActivas(){
        return new ArrayList<>(listaUbicacionesActivas);
    }

    public HashSet<String> getListaUbicacionesRecientes(){ return new HashSet<>(listaUbicacionesRecientes);}

    public void addUbicacion(Ubicacion ubicacion){
        listaUbicaciones.put(ubicacion.getNombre(), ubicacion);
    }

    public boolean addUbicacionReciente(String toponimo) {
        return listaUbicacionesRecientes.add(formatearToponimo(toponimo));
    }

    public boolean deleteUbicacion(String toponimo){
        if(listaUbicaciones.remove(formatearToponimo(toponimo))!=null){
            listaUbicacionesRecientes.remove(formatearToponimo(toponimo));
            listaAlias.remove(formatearToponimo(toponimo));
            listaUbicacionesActivas.remove(formatearToponimo(toponimo));
            return true;
        }
        return false;
    }

    public boolean activarUbicacion(String toponimo){
        if(!listaUbicaciones.containsKey(formatearToponimo(toponimo))){
            return false;
        }
        return listaUbicacionesActivas.add(formatearToponimo(toponimo));
    }

    public boolean desactivarUbicacion(String toponimo){
        if(!listaUbicaciones.containsKey(formatearToponimo(toponimo))){
            return false;
        }
        listaUbicacionesRecientes.remove(formatearToponimo(toponimo));
        return listaUbicacionesActivas.remove(formatearToponimo(toponimo));
    }

    public boolean deleteUbicacionReciente(String toponimo) {
        return listaUbicacionesRecientes.remove(formatearToponimo(toponimo));
    }

    public Ubicacion getUbicacion(String toponimo){
        Ubicacion ubicacion=listaUbicaciones.get(formatearToponimo(toponimo));
        return ubicacion;
    }

    public boolean contieneUbicacion(String toponimo) {
        return listaUbicaciones.get(formatearToponimo(toponimo))!=null;
    }

    public boolean contieneUbicacionActiva(String toponimo){return listaUbicacionesActivas.contains(formatearToponimo(toponimo));}

    public boolean addAlias(String nombre, String alias) {
        ArrayList<String> lista=listaAlias.get(nombre);
        if(lista==null){
            lista=new ArrayList<>();
            lista.add(alias);
            listaAlias.put(nombre,lista);
            return true;
        }
        else if(!lista.contains(alias)){
            lista.add(alias);
            listaAlias.put(nombre,lista);
            return true;
        }
        return false;
    }

    public ArrayList<String> getListaAlias(String nombre) {
        return new ArrayList<>(listaAlias.get(nombre));
    }

    public boolean deleteAlias(String nombre, String alias) {
        ArrayList<String> lista=listaAlias.get(nombre);
        if(lista!=null && lista.remove(alias)){
            listaAlias.put(nombre,lista);
            return true;
        }
        return false;
    }

    public boolean clear(){
        listaUbicaciones.clear();
        listaUbicacionesActivas.clear();
        listaAlias.clear();
        listaUbicacionesRecientes.clear();
        return listaUbicaciones.isEmpty() && listaUbicacionesActivas.isEmpty() && listaAlias.isEmpty() && listaUbicacionesRecientes.isEmpty();
    }
    private String formatearToponimo(String toponimo){
        return toponimo.toLowerCase(Locale.ROOT); //todos los toponimos en minuscula
    }
}
