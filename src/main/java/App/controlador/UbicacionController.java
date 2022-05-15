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
    public String UbicacionesFavoritas(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo){
        LocalDate fecha = LocalDate.now();
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        gestorSQLite.addUbicacionFavorita(toponimo,fecha.toString());

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
        gestorSQLite.deleteUbicacionFavorita(toponimo);

        return "/ubicacion/deleteUbicFav";
    }


    @RequestMapping("/desactivarUbic")
    public String desUbic(@RequestParam(name="nombre",required = false,defaultValue ="") String ubicacion){

        LocalDate fecha = LocalDate.now();
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        gestorSQLite.desactivarUbicacion(ubicacion);
        gestorSQLite.addUbicacionPrevia(ubicacion,fecha.toString());

        return "desactivarUbic";
    }

    @RequestMapping("/activarUbic")
    public String actvUbic(@RequestParam(name="nombre",required = false,defaultValue ="") String ubicacion){

        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        gestorSQLite.activarUbicacion(ubicacion);

        return "activarUbic";
    }

}
