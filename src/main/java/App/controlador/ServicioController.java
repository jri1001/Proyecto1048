package App.controlador;

import modelo.GestorNewsDataIO;
import modelo.GestorOpenWeather;
import modelo.GestorSQLite;
import modelo.GestorTicketMaster;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class ServicioController {

    @RequestMapping("/servicios/list_met")
    public String listMet(@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo,Model model) {

        GestorOpenWeather gestorOpenWeather = new GestorOpenWeather();
        GestorSQLite gestorSQLite = new GestorSQLite();

        String top = gestorSQLite.formatearToponimo(toponimo);
        String city = gestorOpenWeather.peticion(top).get(0).get("City");
        String temperatura = gestorOpenWeather.peticion(top).get(0).get("Temperature");
        String humedad = gestorOpenWeather.peticion(top).get(0).get("Humidity");
        String presion = gestorOpenWeather.peticion(top).get(0).get("Pressure");
        String velocidadAire = gestorOpenWeather.peticion(top).get(0).get("Velocidad aire");
        String clima = gestorOpenWeather.peticion(top).get(0).get("Weather");
        String climadescrp = gestorOpenWeather.peticion(top).get(0).get("Weather description");


        model.addAttribute("ciudad",city);
        model.addAttribute("temp",temperatura);
        model.addAttribute("hum",humedad);
        model.addAttribute("pres",presion);
        model.addAttribute("velaire",velocidadAire);
        model.addAttribute("clim",clima);
        model.addAttribute("descripcion",climadescrp);

        return "servicios/list_met";
    }

    @RequestMapping("/servicios/list_noticias")
    public String listNoticias(Model model){
        // TODO: falta sincronizar con gestor sqlite
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicacionesActivas", gestorSQLite.getListaUbicacionesActivas());
        return "servicios/list_noticias";
    }

    @RequestMapping("/servicios/info")
    public String info (Model model){
        // TODO: falta sincronizar con gestor sqlite
        GestorNewsDataIO gestorNewsDataIO = new GestorNewsDataIO();
        ArrayList<HashMap<String, String>> noticias = gestorNewsDataIO.peticion("valencia");

        if(noticias != null ||  0 < noticias.size()){

            /* Se muestran 3 noticias */
            HashMap<String, String> notic = noticias.get(0);
            model.addAttribute("tit", notic.get("Title"));
            model.addAttribute("link", notic.get("Link"));
            model.addAttribute("desc", notic.get("Description"));
            model.addAttribute("fecha", notic.get("Date"));

            HashMap<String, String> noti = noticias.get(1);
            model.addAttribute("titu", noti.get("Title"));
            model.addAttribute("lin", noti.get("Link"));
            model.addAttribute("des", noti.get("Description"));
            model.addAttribute("fech", noti.get("Date"));

            HashMap<String, String> notc = noticias.get(2);
            model.addAttribute("titul", notc.get("Title"));
            model.addAttribute("lik", notc.get("Link"));
            model.addAttribute("descr", notc.get("Description"));
            model.addAttribute("fec", notc.get("Date"));
        }

        return "servicios/info";
    }

    @RequestMapping("/servicios/eventos")
    public String eventos (Model model){

        GestorTicketMaster gestorTicketMaster = new GestorTicketMaster();
        ArrayList<HashMap<String, String>> eventos = gestorTicketMaster.peticion("valencia");

        if(eventos != null ||  2 < eventos.size()){

            /* Se muestran 3 eventos */
            HashMap<String, String> even1 = eventos.get(0);
            model.addAttribute("name", even1.get("Event name"));
            model.addAttribute("type", even1.get("Type"));
            model.addAttribute("location", even1.get("Location"));
            model.addAttribute("info", even1.get("Information"));

            HashMap<String, String> even2 = eventos.get(1);
            model.addAttribute("nam", even2.get("Event name"));
            model.addAttribute("typ", even2.get("Type"));
            model.addAttribute("locatio", even2.get("Location"));
            model.addAttribute("inf", even2.get("Information"));

            HashMap<String, String> even3 = eventos.get(2);
            model.addAttribute("nom", even3.get("Event name"));
            model.addAttribute("tip", even3.get("Type"));
            model.addAttribute("local", even3.get("Location"));
            model.addAttribute("infor", even3.get("Information"));
        }

        return "servicios/eventos";
    }

    @RequestMapping("/servicios/list_event")
    public String listEventos (@RequestParam(name="nombre",required = false,defaultValue ="") String toponimo,Model model){
        // TODO: falta sincronizar con gestor sqlite
        GestorTicketMaster gestorTicketMaster = new GestorTicketMaster();
        ArrayList<HashMap<String, String>> eventos = gestorTicketMaster.peticion(toponimo);

        if(eventos != null ||  1 < eventos.size()){

            /* Se muestran 2 eventos */
            HashMap<String, String> even1 = new HashMap<>();
            HashMap<String, String> even2 = new HashMap<>();

            for (HashMap e : eventos ) {

                if(e.get("Location").equals(toponimo) && even1.isEmpty()){
                        even1=e;
                }
                if(e.get("Location").equals(toponimo) && even2.isEmpty()){
                    even2=e;
                    if(even2.get("Event name").equals(even1.get("Event name"))){
                         even2 = new HashMap<>();
                    }
                }
            }

            model.addAttribute("name", even1.get("Event name"));
            model.addAttribute("type", even1.get("Type"));
            model.addAttribute("location", even1.get("Location"));
            model.addAttribute("info", even1.get("Information"));

            model.addAttribute("nam", even2.get("Event name"));
            model.addAttribute("typ", even2.get("Type"));
            model.addAttribute("locatio", even2.get("Location"));
            model.addAttribute("inf", even2.get("Information"));

            model.addAttribute("top",toponimo);

            if(even2.isEmpty() || even1.isEmpty()){
                String mensajes = "No hay eventos disponibles";
                model.addAttribute("mensaje",mensajes);
            }

        }



        return "servicios/list_event";
    }


    @RequestMapping("/meteorologia")
    public String Meteorologia(Model model) {
        // TODO: falta sincronizar con gestor sqlite
        GestorSQLite gestorSQLite = new GestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicacionesActivas", gestorSQLite.getListaUbicacionesActivas());

        return "meteorologia";
    }

    @RequestMapping("/meteorologiatopon")
    public String meteorolotop() {
        return "meteorologiatopon";
    }

}
