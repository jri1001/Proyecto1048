package App.controlador;

import modelo.GestorGeocoding;
import modelo.GestorSQLite;
import modelo.Ubicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        GestorGeocoding gestorGeocoding = new GestorGeocoding();
        Ubicacion ubicacion = gestorGeocoding.peticion(coordenadas);

        GestorSQLite gestorSQLite = new GestorSQLite();

        if(ubicacion != null) {
            gestorSQLite.addUbicacion(ubicacion);
            String nombr = ubicacion.getNombre();
            String ali = gestorSQLite.formatearToponimo(alias);
            String nom = gestorSQLite.formatearToponimo(nombr);
            gestorSQLite.addAlias(nom, ali);
        }

        return "ubicacion/add-coordenadas";
    }

    @RequestMapping(value="/ubicacion/add-toponimo")
    public String addToponimo(@RequestParam(name="nombre",required = false,defaultValue ="") String nombr, @RequestParam(name="alias",required = false,defaultValue ="") String alia) {

        GestorGeocoding gestorGeocoding = new GestorGeocoding();
        Ubicacion ubicacion = gestorGeocoding.peticion(nombr);

        GestorSQLite gestorSQLite = new GestorSQLite();

        if(ubicacion != null) {
            gestorSQLite.addUbicacion(ubicacion);
            String ali = gestorSQLite.formatearToponimo(alia);
            String nom = gestorSQLite.formatearToponimo(nombr);
            gestorSQLite.addAlias(nom, ali);
        }

        return "ubicacion/add-toponimo";
    }

    @RequestMapping(value="/ubicacion/add-grupo")
    public String addUbicacGrupo(@RequestParam(name="grupo",required = false,defaultValue ="") String grupo, @RequestParam(name="toponimo",required = false,defaultValue ="") String toponimo, Model model) {
        GestorSQLite gestorSQLite = new GestorSQLite();

        if(gestorSQLite.getGrupos().contains(grupo) && !toponimo.equals("")){
            String nombre = gestorSQLite.UbicacionGrupo(grupo,toponimo);
            gestorSQLite.addUbicacionGrupo(nombre,grupo);
            String mens = "Ubicación añadida al grupo" + " " + grupo;
            model.addAttribute("mensaje",mens);
        }else{
            if(!toponimo.equals("") && !grupo.equals("")){
                String mens = "Grupo incorrecto";
                model.addAttribute("mensaje", mens);
            }
        }
        return "ubicacion/add-grupo";
    }

    @RequestMapping("/ubicacion/infoUbic")
    public String infoUbic(){return "ubicacion/infoUbic";}

    @RequestMapping("/ubicacion/infoUbicacion")
    public String infoUbica(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo,Model model){

        Ubicacion ubic = new Ubicacion();
        GestorSQLite gestorSQLite = new GestorSQLite();
        ubic = gestorSQLite.getUbicacion(toponimo);

        if(ubic != null) {

            model.addAttribute("ciudad", ubic.getCiudad());
            model.addAttribute("provincia", ubic.getProvincia());
            model.addAttribute("codigo", ubic.getCod_postal());
            model.addAttribute("longitud", ubic.getLongitud());
            model.addAttribute("latitud", ubic.getLatitud());
            model.addAttribute("alias", gestorSQLite.getAlias(toponimo));

            if ( ubic.getNombre()== null || !ubic.getNombre().equals(gestorSQLite.formatearToponimo(toponimo))){
                String mensajes = "Topónimo incorrecto.Vuelva intentarlo de nuevo.";
                model.addAttribute("mensaje", mensajes);
            }
        }
        return "ubicacion/infoUbicacion";
    }

    @RequestMapping("/ubicacion/gruposUbicacion")
    public String listGrupos(Model model){
        GestorSQLite gestorSQLite = new GestorSQLite();
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

    @RequestMapping("/ubicacion/list-activas")
    public String listActivas(Model model){
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicacionesActivas", gestorSQLite.getListaUbicacionesActivas());

        return "ubicacion/list-activas";
    }

    @RequestMapping("/ubicacion/list-previas")
    public String listPrevias(Model model){
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicacionesPrevias", gestorSQLite.getListaUbicacionesRecientes());

        return "ubicacion/list-previas";
    }

    @RequestMapping("/ubicacion/mis-ubicaciones")
    public String misUbicaciones(Model model){
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicaciones", gestorSQLite.getUbicacionesFavoritas());

        return "ubicacion/mis-ubicaciones";
    }

    @RequestMapping("/ubicacion/ubicFavorita")
    public String UbicacionesFavoritas(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo,Model model){
        LocalDate fecha = LocalDate.now();
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();

        String topo = gestorSQLite.formatearToponimo(toponimo);

        if (gestorSQLite.getListaUbicacionesActivas().contains(topo)){
            gestorSQLite.addUbicacionFavorita(toponimo, fecha.toString());
            String mens = "Ubicación añadida a favoritos.";
            model.addAttribute("mensaje",mens);
        }else{
            if(!toponimo.equals("")){
                String mens = "Topónimo incorrecto.";
                model.addAttribute("mensaje", mens);
            }
        }

        return "ubicacion/ubicFavorita";
    }

    @RequestMapping("/ubicacion/elimUbic")
    public String descUbic(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo,Model model){
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        gestorSQLite.deleteUbicacion(toponimo);

        return "/ubicacion/elimUbic";
    }

    @RequestMapping("/ubicacion/deleteUbicFav")
    public String deleteUbicacionFavorita(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo,Model model){
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        String topo = gestorSQLite.formatearToponimo(toponimo);

        if (gestorSQLite.getListaUbicacionesActivas().contains(topo)){
            gestorSQLite.deleteUbicacionFavorita(toponimo);
            String mens = "Ubicación eliminada de favoritos.";
            model.addAttribute("mensaje",mens);
        }else{
            if(!toponimo.equals("")){
                String mens = "Topónimo incorrecto.";
                model.addAttribute("mensaje", mens);
            }
        }

        return "/ubicacion/deleteUbicFav";
    }

    @RequestMapping("/ubicacion/deleteGrupo")
    public String deleteUbicacionesGrupo(@RequestParam(name="nombre",required = false,defaultValue ="") String grupo,Model model){
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();

        if(!grupo.equals("") && gestorSQLite.getGrupos().contains(grupo)){
            String mens = "Las ubicaciones del grupo han sido eliminadas.";
            gestorSQLite.clearGrupoUbicaciones(grupo);
            model.addAttribute("mensaje", mens);
        }else{
            if(!grupo.equals("")){
                String mens = "El nombre del grupo es incorrecto.";
                model.addAttribute("mensaje", mens);
            }
        }
        return "/ubicacion/deleteGrupo";
    }


    @RequestMapping("/desactivarUbic")
    public String desUbic(@RequestParam(name="nombre",required = false,defaultValue ="") String ubicacion,Model model){

        LocalDate fecha = LocalDate.now();
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();

        String topo = gestorSQLite.formatearToponimo(ubicacion);

        if(gestorSQLite.getListaUbicacionesActivas().contains(topo)){
            gestorSQLite.desactivarUbicacion(ubicacion);
            gestorSQLite.addUbicacionPrevia(ubicacion, fecha.toString());
            String mens = "La ubicación" + " " + ubicacion + " se ha desactivado.";
            model.addAttribute("mensaje", mens);
        }else{
            if(!ubicacion.equals("")){
                String mens = "Esta ubicación no está activa en el sistema.";
                model.addAttribute("mensaje", mens);
            }
        }

        return "desactivarUbic";
    }

    @RequestMapping("/activarUbic")
    public String actvUbic(@RequestParam(name="nombre",required = false,defaultValue ="") String ubicacion, Model model){

        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();

        if(!ubicacion.equals("")){
            gestorSQLite.activarUbicacion(ubicacion);
            String mens = "La ubicación se ha activado en el sistema.";
            model.addAttribute("mensaje", mens);
        }

        return "activarUbic";
    }

}
