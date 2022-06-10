package modelo;

import java.util.*;

public class GestorMain {
    private IntGestorGeocoding gestorGeocoding;
    private IntGestorUbicacion gestorUbicacion;
    private IntGestorServicios gestorServicios;
    private IntGestorSQLite gestorSQLite;
    private IntGestorTTS gestorTTS;
    private List<String> grupoUbic; //TODO: revisar esto


    public GestorMain() {
        grupoUbic = new ArrayList<>(); //TODO: revisar esto
    }

    public void inject(GestorGeocoding gestorGeocoding){
        this.gestorGeocoding=gestorGeocoding;
    }

    public void inject(GestorUbicacion gestorUbicacion){
        this.gestorUbicacion=gestorUbicacion;
    }

    public void inject(GestorServicios gestorServicios){
        this.gestorServicios=gestorServicios;
    }

    public void inject(GestorSQLite gestorSQLite){
        this.gestorSQLite=gestorSQLite;
    }

    public void inject(GestorTTS gestorTTS){
        this.gestorTTS=gestorTTS;
    }

    //Ubicaciones

    public Map<String, Ubicacion> getListaUbicaciones(){ return gestorUbicacion.getListaUbicacion();}
    public List<String> getListaUbicacionesActivas(){ return gestorUbicacion.getListaUbicacionesActivas();}
    public HashSet<String> getListaUbicacionesRecientes(){ return gestorUbicacion.getListaUbicacionesRecientes();}

    public boolean activarUbicacion(String toponimo){
        if(gestorUbicacion.activarUbicacion(formatearToponimo(toponimo))){
            return gestorSQLite.activarUbicacion(formatearToponimo(toponimo));
        }
        return false;
    }
    public boolean desactivarUbicacion(String toponimo){
        if(gestorUbicacion.desactivarUbicacion(formatearToponimo(toponimo))){
            gestorSQLite.deleteUbicacionReciente(formatearToponimo(toponimo));
            gestorUbicacion.deleteUbicacionReciente(formatearToponimo(toponimo));
            return gestorSQLite.desactivarUbicacion(formatearToponimo(toponimo));
        }
        return false;
    }
    public boolean contieneUbicacion(String toponimo){return  gestorUbicacion.contieneUbicacion(formatearToponimo(toponimo));}

    public boolean clearUbicaciones(){
        return gestorUbicacion.clear() && gestorSQLite.clearUbicaciones();
    }

    public Ubicacion addUbicacion(String toponimo){
        Ubicacion ubicacion=gestorGeocoding.peticion(formatearToponimo(toponimo));
        if(ubicacion!=null){
            if(ubicacion.getCod_postal().equals("{}")){ //para ubicaciones mas pequeñas que ciudades a veces no las obtiene bien si se usa el toponimo
                ubicacion=gestorGeocoding.peticion(formatearCoordenada(ubicacion.getLatitud()).trim()+",%20"+formatearCoordenada(ubicacion.getLongitud()));
            }
            ubicacion.setLatitud(formatearCoordenada(ubicacion.getLatitud()));
            ubicacion.setLongitud(formatearCoordenada(ubicacion.getLongitud()));
            if(gestorSQLite.addUbicacion(ubicacion)){
                gestorUbicacion.addUbicacion(ubicacion);
                return ubicacion;
            }
        }
        return null;
    }

    public Ubicacion addUbicacion(String[] coordenadas){
        return addUbicacion(formatearCoordenada(coordenadas[0]).trim()+",%20"+formatearCoordenada(coordenadas[1]).trim());
    }

    public Ubicacion getUbicacion(String toponimo){
        Ubicacion ubicacion=gestorUbicacion.getUbicacion(formatearToponimo(toponimo));
        if(ubicacion!=null && gestorUbicacion.getListaUbicacionesActivas().contains(formatearToponimo(toponimo))){
            if(gestorUbicacion.getListaUbicacionesRecientes().contains(formatearToponimo(toponimo))){
                gestorUbicacion.deleteUbicacionReciente(formatearToponimo(toponimo));
                gestorSQLite.deleteUbicacionReciente(formatearToponimo(toponimo));
            }else if(gestorUbicacion.getListaUbicacionesRecientes().size()>=10){
                gestorUbicacion.deleteUbicacionReciente(gestorSQLite.deleteUbicacionRecienteMasAntigua());
            }
            gestorUbicacion.addUbicacionReciente(formatearToponimo(toponimo));
            gestorSQLite.addUbicacionReciente(formatearToponimo(toponimo));
        }
        return ubicacion;
    }

    public Ubicacion getUbicacion(String[] coordenadas){
        return gestorSQLite.getUbicacion(new String[]{formatearCoordenada(coordenadas[0]),formatearCoordenada(coordenadas[1])});
    }

    public boolean deleteUbicacion(String toponimo){
        return gestorUbicacion.deleteUbicacion(formatearToponimo(toponimo)) && gestorSQLite.deleteUbicacion(formatearToponimo(toponimo));
    }

    public String[] getCoordenadasUbicacion(String toponimo) {
        if(getUbicacion(formatearToponimo(toponimo)) != null) {
            String[] coordenadas = new String[2];
            coordenadas[0] = gestorUbicacion.getUbicacion(formatearToponimo(toponimo)).getLatitud();
            coordenadas[1] = gestorUbicacion.getUbicacion(formatearToponimo(toponimo)).getLongitud();
            return new String[]{coordenadas[0],coordenadas[1]};
        }
        return null;
    }

    public String getToponimoCoordenadas(String[] coordenadas) {
        Ubicacion ubicacion=gestorGeocoding.peticion(formatearCoordenada(coordenadas[0]).trim()+",%20"+formatearCoordenada(coordenadas[1]).trim());
        if(ubicacion != null) {
            return formatearToponimo(ubicacion.getNombre());
        }
        return null;
    }

    public ArrayList<String> getListaAlias(String ubicacion) {
        return gestorUbicacion.getListaAlias(formatearToponimo(ubicacion));
    }

    public boolean addAlias(String nombre, String alias) {
        if(gestorUbicacion.addAlias(formatearToponimo(nombre),alias)){
            return gestorSQLite.addAlias(formatearToponimo(nombre),alias);
        }
        return false;
    }

    public boolean deleteAlias(String nombre, String alias) {
        if(gestorUbicacion.deleteAlias(formatearToponimo(nombre),alias)){
            return gestorSQLite.deleteAlias(formatearToponimo(nombre),alias);
        }
        return false;
    }

    public boolean crearGrupoUbic(List<String> ubicaciones) { //todo:revisar esto
        if(ubicaciones.size() < 2)
            return false;
        grupoUbic.addAll(ubicaciones);
        return true;
    }

    public boolean eliminarGrupo(List<String> ubicaciones) { //todo:revisar esto
        if(ubicaciones.size() < 1)
            return false;
        grupoUbic.removeAll(ubicaciones);
        return true;
    }

    //Servicios
    public HashMap<String,HashMap<String,ArrayList<HashMap<String,String>>>> peticionSimultaneaUbicaciones(List<String> listaUbicaciones){
        HashMap<String,HashMap<String,ArrayList<HashMap<String,String>>>> mapa=new HashMap<>();
        if(listaUbicaciones.size()<=3){
            for(String ubicacion: listaUbicaciones){
                if(gestorUbicacion.contieneUbicacion(formatearToponimo(ubicacion))){
                    mapa.put(formatearToponimo(ubicacion),gestorServicios.peticion(formatearToponimo(ubicacion)));
                }
            }
        }
        return mapa;
    }

    public HashMap<String,ArrayList<HashMap<String,String>>> infoUbicacionServicios(String ubicacion){
        Ubicacion ubicacion1=gestorGeocoding.peticion(formatearToponimo(ubicacion));
        if (ubicacion1!=null){
            return gestorServicios.consulta(ubicacion1.getNombre());
        }
        return new HashMap<>();
    }

    public HashMap<String,ArrayList<HashMap<String,String>>> infoUbicacionServicios(String[] coordenadas){
        return infoUbicacionServicios(formatearCoordenada(coordenadas[0]).trim()+",%20"+formatearCoordenada(coordenadas[1]).trim());
    }

    public HashMap<String,ArrayList<HashMap<String,String>>> infoUbicacionActivaServicios(String ubicacion){
        if(gestorUbicacion.getListaUbicacionesActivas().contains(formatearToponimo(ubicacion))){
            return infoUbicacionServicios(formatearToponimo(ubicacion));
        }
        return new HashMap<>();
    }

    public List<String> getListaServicios() {
        return gestorServicios.getListaServicios();
    }

    public List<String> getListaServiciosActivos() {
        return gestorServicios.getListaServiciosActivos();
    }

    public List<String> getListaServiciosUbicacion(String ubicacion) {
        return gestorServicios.getListaServiciosUbicacion(formatearToponimo(ubicacion));
    }

    public boolean clearServicios() {
        if(gestorServicios.clearServiciosActivos()){
            return gestorSQLite.clearServiciosActivos();
        }
        return false;
    }

    public boolean addServicioUbicacion(String servicio, String ubicacion) {
        if(gestorServicios.addServicioUbicacion(servicio, formatearToponimo(ubicacion))){
            return gestorSQLite.addServicioUbicacion(servicio, formatearToponimo(ubicacion));
        }
        return false;
    }



    public boolean deleteServicioUbicacion(String servicio, String ubicacion) {
        if(gestorServicios.deleteServicioUbicacion(servicio, formatearToponimo(ubicacion))){
            return gestorSQLite.deleteServicioUbicacion(servicio, formatearToponimo(ubicacion));
        }
        return false;
    }

    //creo que este no se usa?
    public boolean deleteServicio(String servicio) {
        if(gestorServicios.deleteServicio(servicio)){
            return gestorSQLite.deleteServicioActivo(servicio);
        }
        return false;
    }

    public boolean activarServicio(String servicio) {
        if(gestorServicios.activarServicio(servicio)){
            return gestorSQLite.addServicioActivo(servicio);
        }
        return false;
    }

    public boolean desactivarServicio(String servicio) {
        if(gestorServicios.desactivarServicio(servicio)){
            return gestorSQLite.deleteServicioActivo(servicio);
        }
        return false;
    }

    public HashMap<String,String > getinfoServicios(){
        return gestorServicios.infoServicios();
    }

    //Gestores

    public void syncDB(){
        gestorUbicacion.syncDB(gestorSQLite.getListaUbicaciones(), gestorSQLite.getListaUbicacionesActivas(), gestorSQLite.getListaUbicacionesRecientes(), gestorSQLite.getListaAlias());
        gestorServicios.syncDB(gestorSQLite.getListaServiciosActivos(), gestorSQLite.getListaServiciosUbicacion());
    }

    public boolean crearDB(String nombre){
        return gestorSQLite.crearDB(nombre);
    }

    public  boolean cambiarDB(String myUrl){
        if(myUrl.endsWith(".db")){
            return gestorSQLite.cambiarDB(myUrl);
        }
        return false;
    }

    public boolean copyDB(String myUrl){
        return gestorSQLite.copyDB(myUrl);
    }


    public boolean verificarGestor(String gestor){
        switch (gestor){
            case "Geocoding":
                if(gestorGeocoding==null){
                    return false;
                }
                return gestorGeocoding.peticion("Valencia")!=null;
            case "TTS":
                return gestorTTS!=null;
            default : //esto requiere que la Ubicacion valencia este añadida y tenga al menos un servicio activo asignado
                if(gestorServicios==null){
                    return false;
                }
                return gestorServicios.peticion("Valencia")!=null;
        }
    }

    public void speak(String texto){
        if(texto!=null){
            gestorTTS.speak(texto);
        }
    }

    public String formatearCoordenada(String coordenada){
     return String.format(Locale.ROOT,"%.5f",Double.parseDouble(coordenada)); //las coordenadas tiene precision de 5 decimales
    }

    public String formatearToponimo(String toponimo){
        return toponimo.toLowerCase(Locale.ROOT); //todos los toponimos en minuscula
    }
}
