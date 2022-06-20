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
        String descrip1 = null, descrip2 = null, descrip3 = null;

        if(ubic1 != ""){
            if(gestorOpenWeather.peticion(ubic1).size()>0){
                temp1 = gestorOpenWeather.peticion(ubic1).get(0).get("Temperature");
                descrip1 = gestorOpenWeather.peticion(ubic1).get(0).get("Weather description");
            } else{temp1="--";descrip1="Temperatura no disponible en este momento.";}
            model.addAttribute("ubicacion1",ubic1);
        }else{
            ubic1 = " ";
            model.addAttribute("ubicacion1",ubic1);
        }

        if(ubic2 != "") {
            if(gestorOpenWeather.peticion(ubic2).size()>0){
                temp2 = gestorOpenWeather.peticion(ubic2).get(0).get("Temperature");
                descrip2 = gestorOpenWeather.peticion(ubic2).get(0).get("Weather description");
            } else{temp2="--";descrip2="Temperatura no disponible en este momento.";}
            model.addAttribute("ubicacion2",ubic2);
        }else{
            ubic2 = " ";
            model.addAttribute("ubicacion2",ubic2);

        }
        if(ubic3 != "") {
            if(gestorOpenWeather.peticion(ubic3).size()>0){
                temp3 = gestorOpenWeather.peticion(ubic3).get(0).get("Temperature");
                descrip3 = gestorOpenWeather.peticion(ubic3).get(0).get("Weather description");
            } else{temp3="--";descrip3="Temperatura no disponible en este momento.";}
            model.addAttribute("ubicacion3",ubic3);
        }else{
            ubic3 = " ";
            model.addAttribute("ubicacion3",ubic3);
        }


        if(temp1 != null){
            model.addAttribute("temp1",temp1 + "ºC");
        }else{
            temp1 = " ";
            model.addAttribute("temp1",temp1 + ubic1);
        }

        if(temp2 != null){
            model.addAttribute("temp2",temp2 + "ºC");
        }else{
            temp2 = " ";
            model.addAttribute("temp2",temp2 + ubic2);
        }

        if(temp3 != null){
            model.addAttribute("temp3",temp3 + "ºC");
        }else{
            temp3 = " ";
            model.addAttribute("temp3",temp3 + ubic3 );
        }

        if(descrip1 != null){
            model.addAttribute("descrip1",descrip1 + "");
        }else{
            descrip1 = " ";
            model.addAttribute("descrip1",descrip1 + "");
        }

        if(descrip2 != null){
            model.addAttribute("descrip2",descrip2 + "");
        }else{
            descrip2 = " ";
            model.addAttribute("descrip2",descrip2 + "");
        }

        if(descrip3 != null){
            model.addAttribute("descrip3",descrip3 + "");
        }else{
            descrip3 = " ";
            model.addAttribute("descrip3",descrip3 + "");;
        }

        return "principal";
    }

}
