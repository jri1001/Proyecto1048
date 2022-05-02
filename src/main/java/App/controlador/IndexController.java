package App.controlador;

import modelo.GestorGeocoding;
import modelo.GestorOpenWeather;
import modelo.GestorSQLite;
import modelo.Ubicacion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @RequestMapping(value="/principal")
    public String getPrincipal(Model model) {

        GestorSQLite gestorSQLite = new GestorSQLite();
        GestorOpenWeather gestorOpenWeather = new GestorOpenWeather();
        gestorSQLite.connect();

        String top = gestorSQLite.formatearToponimo("Castellón de la plana");
        String temperatura = gestorOpenWeather.peticion(top).get(0).get("Temperature");
        String ubic1 = "";
        String ubic2 = "";
        String ubic3 = "";

        for(String e : gestorSQLite.getListaUbicacionesActivas()){
            if(ubic1=="") {
                ubic1 = e;
            }else if (ubic2 == ""){
                ubic2 = e;
            }else if(ubic3 == ""){
                ubic3 = e;
            }
        }
        model.addAttribute("ubicacion1",ubic1);
        model.addAttribute("ubicacion2",ubic2);
        model.addAttribute("ubicacion3",ubic3);
        model.addAttribute("temp",temperatura);

        return "principal";
    }

}
