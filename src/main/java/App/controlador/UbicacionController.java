package App.controlador;

import modelo.GestorGeocoding;
import modelo.GestorSQLite;
import modelo.Ubicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//import jakarta.ws.rs.PathParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;


@Controller
public class UbicacionController {

    @RequestMapping("/ubicacion/add")
    public String add(){
        return "ubicacion/add";
    }

    @RequestMapping("/ubicacion/add-coordenadas")
    public String addCoordenadas(@RequestParam(name="latitud",required = false,defaultValue ="") String latitud,@RequestParam(name="longitud",required = false,defaultValue ="") String longitud,@RequestParam(name="alias",required = false,defaultValue ="") String alias,Model model){
        //model.addAttribute("ubicacion", new Ubicacion());

        String coordenadas = latitud + ","+ longitud;

        GestorGeocoding gestorGeocoding = GestorGeocoding.getGestorGeocoding();
        Ubicacion ubicacion = gestorGeocoding.peticion(coordenadas);

        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();

        if(ubicacion != null) {
            if(gestorSQLite.getUbicacion(new String[]{latitud,longitud}).getNombre() == null ) {
                gestorSQLite.addUbicacion(ubicacion);
                String nombr = ubicacion.getNombre();
                String ali = gestorSQLite.formatearToponimo(alias);
                String nom = gestorSQLite.formatearToponimo(nombr);
                gestorSQLite.addAlias(nom, ali);
                String mens = "La ubicación se añadido al sistema";
                model.addAttribute("mensaje", mens);
            }else{
                String mens = "Error: La ubicación ya está dada de alta en el sistema";
                model.addAttribute("mensaje", mens);
            }
        }else{
            if(!latitud.equals("") || !longitud.equals("")){
                String mens = "Error: La latitud o longitud es inválida.";
                model.addAttribute("mensaje",mens);
            }
        }
        return "ubicacion/add-coordenadas";
    }

    @RequestMapping(value="/ubicacion/add-toponimo")
    public String addToponimo(@RequestParam(name="nombre",required = false,defaultValue ="") String nombr, @RequestParam(name="alias",required = false,defaultValue ="") String alia,Model model) {

        GestorGeocoding gestorGeocoding = GestorGeocoding.getGestorGeocoding();
        Ubicacion ubicacion = gestorGeocoding.peticion(nombr);

        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();;

        if(ubicacion != null ) {

            if(gestorSQLite.getUbicacion(nombr).getNombre() == null ) {
                gestorSQLite.addUbicacion(ubicacion);
                String ali = gestorSQLite.formatearToponimo(alia);
                String nom = gestorSQLite.formatearToponimo(nombr);
                gestorSQLite.addAlias(nom, ali);
                String mens = "La ubicación se añadido al sistema";
                model.addAttribute("mensaje", mens);
            }else{
                String mens = "Error: La ubicación ya está dada de alta en el sistema";
                model.addAttribute("mensaje", mens);
            }
        }else{
            if(!nombr.equals("")){
                String mens = "Error: Nombre de ubicación inválido.";
                model.addAttribute("mensaje",mens);
            }
        }
        return "ubicacion/add-toponimo";
    }

    @RequestMapping(value="/ubicacion/add-grupo")
    public String addUbicacGrupo(@RequestParam(name="grupo",required = false,defaultValue ="") String grupo, @RequestParam(name="toponimo",required = false,defaultValue ="") String toponimo, Model model) {
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();;

        if(gestorSQLite.getGrupos().contains(grupo) && !toponimo.equals("")){

            if(gestorSQLite.getListaUbicacionesNombre().contains(gestorSQLite.formatearToponimo(toponimo))) {
                String nombre = gestorSQLite.UbicacionGrupo(grupo, toponimo);
                gestorSQLite.addUbicacionGrupo(nombre, grupo);
                String mens = "Ubicación añadida al grupo" + " " + grupo;
                model.addAttribute("mensaje", mens);
            }else{
                String mens = "Error: Esta ubicación no existe en el sistema";
                model.addAttribute("mensaje", mens);
            }
        }else{
            if(!toponimo.equals("") && !grupo.equals("")){
                String mens = "Error: Grupo incorrecto";
                model.addAttribute("mensaje", mens);
            }
        }
        return "ubicacion/add-grupo";
    }

    @RequestMapping("/ubicacion/infoUbic")
    public String infoUbic(){return "ubicacion/infoUbic";}

    @RequestMapping(value = "/ubicacion/infoUbicacion/{toponimo}")
    public String infoUbica(@PathVariable("toponimo") String toponimo,Model model){
        LocalDate fecha = LocalDate.now();
        Ubicacion ubic = new Ubicacion();
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();;
        ubic = gestorSQLite.getUbicacion(toponimo);

        if(ubic != null) {

            HashMap<String,String> listaServicios = new HashMap<>();
            if(gestorSQLite.getListaServiciosUbicacion().get(toponimo) != null) {
                for (String servicio : gestorSQLite.getListaServiciosUbicacion().get(toponimo)) {
                    if (servicio.equals("NewsDataIO")) {
                        String ruta = "../../servicios/info/"+toponimo;
                        listaServicios.put(servicio, ruta);
                    }
                    if (servicio.equals("TicketMaster")) {
                        String ruta = "../../servicios/list_event/"+ toponimo;
                        listaServicios.put(servicio, ruta);
                    }
                    if (servicio.equals("OpenWeather")) {
                        String ruta = "../../servicios/list_met/" + toponimo;
                        listaServicios.put(servicio, ruta);
                    }
                }
            }

            model.addAttribute("toponimo", toponimo);
            model.addAttribute("listaServicios",listaServicios);
            model.addAttribute("ciudad", ubic.getCiudad());
            model.addAttribute("provincia", ubic.getProvincia());
            model.addAttribute("codigo", ubic.getCod_postal());
            model.addAttribute("longitud", ubic.getLongitud());
            model.addAttribute("latitud", ubic.getLatitud());
            model.addAttribute("alias", gestorSQLite.getAlias(toponimo));
            gestorSQLite.addUbicacionPrevia(toponimo, fecha.toString());

            if ( ubic.getNombre()== null || !ubic.getNombre().equals(gestorSQLite.formatearToponimo(toponimo))){
                String mensajes = "Error: Topónimo incorrecto.Vuelva intentarlo de nuevo.";
                model.addAttribute("mensaje", mensajes);
            }
        }

        return "ubicacion/infoUbicacion";
    }

    @RequestMapping("/ubicacion/gruposUbicacion")
    public String listGrupos(Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        String grupo1 = gestorSQLite.getListaGruposUbicaciones().get(0);
        String ubicGrupo1 = gestorSQLite.getListaGruposUbicaciones().get(1);
        String grupo2 = gestorSQLite.getListaGruposUbicaciones().get(2);
        String ubicGrupo2 = gestorSQLite.getListaGruposUbicaciones().get(3);
        String grupo3 = gestorSQLite.getListaGruposUbicaciones().get(4);
        String ubicGrupo3 = gestorSQLite.getListaGruposUbicaciones().get(5);

        model.addAttribute("Visitadas", grupo1);
        model.addAttribute("ubicVisitadas", ubicGrupo1);
        model.addAttribute("NoVisitadas", grupo2);
        model.addAttribute("NoubicVisitadas", ubicGrupo2);
        model.addAttribute("Favoritos", grupo3);
        model.addAttribute("ubicFavoritas", ubicGrupo3);

        return "ubicacion/gruposUbicacion";
    }

    @RequestMapping(value = "/ubicacion/infoUbicacion/{toponimo}/{servic}")
    public String infoUbica(@PathVariable("toponimo") String toponimo,@PathVariable("servic") String servic,Model model){
        LocalDate fecha = LocalDate.now();
        Ubicacion ubic = new Ubicacion();
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();;
        ubic = gestorSQLite.getUbicacion(toponimo);

        if(gestorSQLite.getListaServiciosUbicacion().get(toponimo).contains(servic)){
            gestorSQLite.deleteServicioUbicacion(servic,toponimo);
        }

        return "redirect:/ubicacion/infoUbicacion/{toponimo}";
    }


    @RequestMapping(value = "/ubicacion/list-activas")
    public String listActivas(Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        model.addAttribute("ubicacionesActivas", gestorSQLite.getListaUbicacionesActivas());

        return "ubicacion/list-activas";
    }

    @RequestMapping(value = "/ubicacion/list-activas/{toponimo}")
    public String desactivarUbic(@PathVariable("toponimo") String toponimo,Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        gestorSQLite.desactivarUbicacion(toponimo);
        model.addAttribute("ubicacionesActivas", gestorSQLite.getListaUbicacionesActivas());

        //System.out.printf("El valor del nombre es " + toponimo);

        return "redirect:/ubicacion/list-activas";
    }

    @RequestMapping("/ubicacion/list-NoActivas")
    public String listNoActivas(Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicacionesNoactivas", gestorSQLite.getListaUbicacionesNoActivas());

        return "ubicacion/list-NoActivas";
    }
    @RequestMapping("/ubicacion/list-NoActivas/{toponimo}")
    public String activarUbic(@PathVariable("toponimo") String toponimo,Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        gestorSQLite.activarUbicacion(toponimo);
        model.addAttribute("ubicacionesNoactivas", gestorSQLite.getListaUbicacionesNoActivas());

        return "redirect:/ubicacion/list-NoActivas";
    }

    @RequestMapping("/ubicacion/list-previas")
    public String listPrevias(Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicacionesPrevias", gestorSQLite.getListaUbicacionesRecientes());

        return "ubicacion/list-previas";
    }

    @RequestMapping("/ubicacion/mis-ubicaciones")
    public String misUbicaciones(Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicaciones", gestorSQLite.getUbicacionesFavoritas());

        return "ubicacion/mis-ubicaciones";
    }

    @RequestMapping("/ubicacion/ubicFavorita")
    public String UbicacionesFavoritas(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo,Model model){
        LocalDate fecha = LocalDate.now();
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        String topo = gestorSQLite.formatearToponimo(toponimo);

        if (gestorSQLite.getListaUbicacionesActivas().contains(topo)){
            gestorSQLite.addUbicacionFavorita(toponimo, fecha.toString());
            String mens = "Ubicación añadida a favoritos.";
            model.addAttribute("mensaje",mens);
        }else{
            if(!toponimo.equals("") && gestorSQLite.getUbicacion(toponimo).getNombre()== null){
                String mens = "Error: No se ha podido añadir a favoritos. La ubicación no está activada";
                model.addAttribute("mensaje", mens);
            }else{
                if(!toponimo.equals("")) {
                    String mens = "Error: Topónimo incorrecto.";
                    model.addAttribute("mensaje", mens);
                }
            }
        }

        return "ubicacion/ubicFavorita";
    }

    @RequestMapping("/ubicacion/elimUbic")
    public String descUbic(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo,Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        if(!toponimo.equals("") && gestorSQLite.getUbicacion(toponimo).getNombre()!=null && gestorSQLite.getUbicacion(toponimo).getNombre().equals(gestorSQLite.formatearToponimo(toponimo))) {
            gestorSQLite.deleteUbicacion(toponimo);
            String mens = "La ubicación se ha eliminado del sistema.";
            model.addAttribute("mensaje", mens);
        }else{
            if(!toponimo.equals("")){
                String mens = "Error: La ubicación no existe en el sistema.";
                model.addAttribute("mensaje", mens);
            }
        }
        return "/ubicacion/elimUbic";
    }

    @RequestMapping("/ubicacion/deleteUbicFav")
    public String deleteUbicacionFavorita(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo,Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();
        String topo = gestorSQLite.formatearToponimo(toponimo);

        if (gestorSQLite.getListaUbicacionesActivas().contains(topo)){
            gestorSQLite.deleteUbicacionFavorita(toponimo);
            String mens = "Ubicación eliminada de favoritos.";
            model.addAttribute("mensaje",mens);
        }else{
            if(!toponimo.equals("")){
                String mens = "Error: Topónimo incorrecto.";
                model.addAttribute("mensaje", mens);
            }
        }

        return "/ubicacion/deleteUbicFav";
    }

    @RequestMapping("/ubicacion/deleteGrupo")
    public String deleteUbicacionesGrupo(@RequestParam(name="nombre",required = false,defaultValue ="") String grupo,Model model){
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        if(!grupo.equals("") && gestorSQLite.getGrupos().contains(grupo)){
            String mens = "Las ubicaciones del grupo han sido eliminadas.";
            gestorSQLite.clearGrupoUbicaciones(grupo);
            model.addAttribute("mensaje", mens);
        }else{
            if(!grupo.equals("")){
                String mens = "Error: El nombre del grupo es incorrecto.";
                model.addAttribute("mensaje", mens);
            }
        }
        return "/ubicacion/deleteGrupo";
    }


    @RequestMapping("/desactivarUbic")
    public String desUbic(@RequestParam(name="nombre",required = false,defaultValue ="") String ubicacion,Model model){

        LocalDate fecha = LocalDate.now();
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        String topo = gestorSQLite.formatearToponimo(ubicacion);

        if(gestorSQLite.getListaUbicacionesActivas().contains(topo)){
            gestorSQLite.desactivarUbicacion(ubicacion);
            gestorSQLite.addUbicacionPrevia(ubicacion, fecha.toString());
            String mens = "La ubicación" + " " + ubicacion + " se ha desactivado.";
            model.addAttribute("mensaje", mens);
        }else{
            if(!ubicacion.equals("")){
                String mens = "Error: Esta ubicación no está activa en el sistema.";
                model.addAttribute("mensaje", mens);
            }
        }

        return "desactivarUbic";
    }

    @RequestMapping("/activarUbic")
    public String actvUbic(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo, Model model){

        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        if(!toponimo.equals("") && gestorSQLite.getUbicacion(toponimo).getNombre()!=null && gestorSQLite.getUbicacion(toponimo).getNombre().equals(gestorSQLite.formatearToponimo(toponimo))){

            if(!gestorSQLite.getListaUbicacionesActivas().contains(gestorSQLite.formatearToponimo(toponimo))){
                gestorSQLite.activarUbicacion(toponimo);
                String mens = "La ubicación se ha activado en el sistema.";
                model.addAttribute("mensaje", mens);
            }else{
                String mens = "Error: Esta ubicación ya está activa en el sistema.";
                model.addAttribute("mensaje", mens);
            }

        }else{
            if(!toponimo.equals("")){
                String mens = "Error: La ubicación no está añadida en el sistema.";
                model.addAttribute("mensaje", mens);
            }
        }
        return "activarUbic";
    }

}
