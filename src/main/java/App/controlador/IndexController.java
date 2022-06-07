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

        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        GestorOpenWeather gestorOpenWeather = GestorOpenWeather.getGestorOpenWeather();
        //gestorSQLite.crearDB("proyecto.db");
        gestorSQLite.connect();

        String top = gestorSQLite.formatearToponimo("Castellón de la plana");
       // String temperatura = gestorOpenWeather.peticion(top).get(0).get("Temperature");
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

        String temp1 = null, temp2 = null, temp3 = null;
        if(ubic1 != ""){
            temp1 = gestorOpenWeather.peticion(ubic1).get(0).get("Temperature");
            model.addAttribute("ubicacion1",ubic1);
        }else{
            ubic1 = " ";
            model.addAttribute("ubicacion1",ubic1);
        }

        if(ubic2 != "") {
            temp2 = gestorOpenWeather.peticion(ubic2).get(0).get("Temperature");
            model.addAttribute("ubicacion2",ubic2);
        }else{
            ubic2 = " ";
            model.addAttribute("ubicacion2",ubic2);

        }
        if(ubic3 != "") {
            temp3 = gestorOpenWeather.peticion(ubic2).get(0).get("Temperature");
            model.addAttribute("ubicacion3",ubic3);
        }else{
            ubic3 = " ";
            model.addAttribute("ubicacion3",ubic3);
        }


        if(temp1 != null){
            model.addAttribute("temp1",temp1 + "ºC ("+ubic1+")");
        }else{
            temp1 = " ";
            model.addAttribute("temp1",temp1 + ubic1);
        }
        if(temp2 != null){
            model.addAttribute("temp2",temp2 + "ºC ("+ubic2+")");
        }else{
            temp2 = " ";
            model.addAttribute("temp2",temp2 + ubic2);
        }
        if(temp3 != null){
            model.addAttribute("temp3",temp3 + "ºC ("+ubic3+")");
        }else{
            temp3 = " ";
            model.addAttribute("temp3",temp3 + ubic3 );
        }

        return "principal";
    }

}
