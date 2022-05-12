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

    @RequestMapping("/ubicacion/add-coordenadas")       //  TODO: falta sincronizar con metodo add gestor sqlite
    public String addCoordenadas(Model model){
        //model.addAttribute("ubicacion", new Ubicacion());

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