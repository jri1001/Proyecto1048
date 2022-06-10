package App.controlador;

import modelo.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@Controller
public class ServicioController {


    @RequestMapping(value = "/servicios/list_met/{toponimo}")
    public String listMet(@PathVariable("toponimo") String toponimo, @RequestParam(name="modo",required = false,defaultValue ="") String modo, Model model){

        GestorOpenWeather gestorOpenWeather = GestorOpenWeather.getGestorOpenWeather();
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();;
        String topo = gestorSQLite.formatearToponimo(toponimo);

        GestorTTS gestorTTS = GestorTTS.getGestorTTS();

        if(toponimo !="" && gestorSQLite.getListaServiciosActivos().contains("OpenWeather") && gestorSQLite.getListaUbicacionesActivas().contains(topo)) {
            String top = gestorSQLite.formatearToponimo(toponimo);
            String city = gestorOpenWeather.peticion(top).get(0).get("City");
            String temperatura = gestorOpenWeather.peticion(top).get(0).get("Temperature");
            String humedad = gestorOpenWeather.peticion(top).get(0).get("Humidity");
            String presion = gestorOpenWeather.peticion(top).get(0).get("Pressure");
            String velocidadAire = gestorOpenWeather.peticion(top).get(0).get("Velocidad aire");
            String clima = gestorOpenWeather.peticion(top).get(0).get("Weather");
            String climadescrp = gestorOpenWeather.peticion(top).get(0).get("Weather description");

            model.addAttribute("ciudad", city);
            model.addAttribute("temp", temperatura);
            model.addAttribute("hum", humedad);
            model.addAttribute("pres", presion);
            model.addAttribute("velaire", velocidadAire);
            model.addAttribute("clim", clima);
            model.addAttribute("descripcion", climadescrp);

            LocalDate fecha = LocalDate.now();   //Historial de ubicaciones
            gestorSQLite.addUbicacionPrevia(toponimo, fecha.toString());

            if(modo.equals("Si") || modo.equals("si") ){
                gestorTTS.speak("ciudad");
                gestorTTS.speak(city);
                gestorTTS.speak("temperatura");gestorTTS.speak(temperatura);
                gestorTTS.speak("humedad");gestorTTS.speak(humedad);
                gestorTTS.speak("Presion");gestorTTS.speak(presion);
                gestorTTS.speak("Velocidad del aire");gestorTTS.speak(velocidadAire);
                gestorTTS.speak("clima");gestorTTS.speak(clima);
                gestorTTS.speak("descripcion");gestorTTS.speak(climadescrp);
            }

        }else{
            String e = " ";
            model.addAttribute("ciudad", e);
            model.addAttribute("temp", e);
            model.addAttribute("hum", e);
            model.addAttribute("pres", e);
            model.addAttribute("velaire", e);
            model.addAttribute("clim", e);
            model.addAttribute("descripcion", e);

            if(!gestorSQLite.getListaServiciosActivos().contains("OpenWeather")){
                String serv = "Error: Este servicio no está activado en el sistema.";
                model.addAttribute("mensaje",serv);
            }else{
                String serv = "Error: Ubicación no dada de alta en el sistema.";
                model.addAttribute("mensaje",serv);
            }

        }

        return "servicios/list_met";
    }

    @RequestMapping("/servicios/servicios-activos")
    public String ServiciosActivos(Model model){
        GestorServicios gestorServicios = GestorServicios.getGestorServicios();
        model.addAttribute("serviciosActivos", gestorServicios.ServiciosActivos());

        return "servicios/servicios-activos";
    }

    @RequestMapping("/servicios/InfoServicios")
    public String InfoServicios(@RequestParam(name="speech",required = false,defaultValue ="") String modo,Model model) {

        GestorServicios gestorServicios = GestorServicios.getGestorServicios();

        Set<String> servicios = gestorServicios.infoServicios().keySet();
        String name1 =null;
        String name2 =null;
        String name3 =null;
        String name4 =null;
        String desc1 =null;
        String desc2 =null;
        String desc3 =null;
        String desc4 =null;

        for (String serv : servicios){
            if(name1==null){
                name1=serv;
                desc1=gestorServicios.infoServicios().get(serv);
            }else if(name2==null){
                name2=serv;
                desc2=gestorServicios.infoServicios().get(serv);
            }else if(name3==null){
                name3=serv;
                desc3=gestorServicios.infoServicios().get(serv);
            }else{
                name4=serv;
                desc4=gestorServicios.infoServicios().get(serv);
            }
        }

        model.addAttribute("nameserv1",name1);
        model.addAttribute("descserv1",desc1);
        model.addAttribute("nameserv2",name2);
        model.addAttribute("descserv2",desc2);
        model.addAttribute("nameserv3",name3);
        model.addAttribute("descserv3",desc3);
        model.addAttribute("nameserv4",name4);
        model.addAttribute("descserv4",desc4);

        if(modo.equals("Si")){
            GestorTTS gestorTTS = GestorTTS.getGestorTTS();
            gestorTTS.speak("nombre del servicio");gestorTTS.speak(name1);
            gestorTTS.speak("descripcion");gestorTTS.speak(desc1);
            gestorTTS.speak("nombre del servicio");gestorTTS.speak(name2);
            gestorTTS.speak("descripcion");gestorTTS.speak(desc2);
            gestorTTS.speak("nombre del servicio");gestorTTS.speak(name3);
            gestorTTS.speak("descripcion");gestorTTS.speak(desc3);
            gestorTTS.speak("nombre del servicio");gestorTTS.speak(name4);
            gestorTTS.speak("descripcion");gestorTTS.speak(desc4);

        }

        return "servicios/InfoServicios";
    }

    @RequestMapping("/servicios/list_noticias")
    public String listNoticias(Model model){

        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicacionesActivas", gestorSQLite.getListaUbicacionesActivas());
        return "servicios/list_noticias";
    }

    @RequestMapping("/servicios/info")
    public String info (@RequestParam(name="speech",required = false,defaultValue ="") String modo,@RequestParam(name="speech1",required = false,defaultValue ="") String modo1,@RequestParam(name="speech2",required = false,defaultValue ="") String modo2,Model model){

        GestorNewsDataIO gestorNewsDataIO = GestorNewsDataIO.getGestorNewsDataIO();
        ArrayList<HashMap<String, String>> noticias = gestorNewsDataIO.peticion("valencia");
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();

        if(noticias != null &&  0 < noticias.size() && gestorSQLite.getListaServiciosActivos().contains("NewsDataIO")){

            /* Se muestran 3 noticias */
            HashMap<String, String> notic = noticias.get(0);
            model.addAttribute("tit", notic.get("Title"));
            model.addAttribute("link", notic.get("Link"));
            model.addAttribute("desc", notic.get("Description"));
            model.addAttribute("fecha", notic.get("Date"));

            if(modo.equals("uno")){
                GestorTTS gestorTTS = GestorTTS.getGestorTTS();
                gestorTTS.speak("titulo");gestorTTS.speak(notic.get("Title"));
                gestorTTS.speak("link");gestorTTS.speak(notic.get("Link"));
                gestorTTS.speak("descripcion");gestorTTS.speak(notic.get("Description"));
                gestorTTS.speak("fecha");gestorTTS.speak(notic.get("Date"));
            }
            modo = "";

            if( 1 < noticias.size()) {
                HashMap<String, String> noti = noticias.get(1);
                model.addAttribute("titu", noti.get("Title"));
                model.addAttribute("lin", noti.get("Link"));
                model.addAttribute("des", noti.get("Description"));
                model.addAttribute("fech", noti.get("Date"));

                if(modo1.equals("dos")){
                    GestorTTS gestorTTS = GestorTTS.getGestorTTS();
                    gestorTTS.speak("titulo");gestorTTS.speak(noti.get("Title"));
                    gestorTTS.speak("link");gestorTTS.speak(noti.get("Link"));
                    gestorTTS.speak("descripcion");gestorTTS.speak(noti.get("Description"));
                    gestorTTS.speak("fecha");gestorTTS.speak(noti.get("Date"));
                }

                if( 2 < noticias.size()) {
                    HashMap<String, String> notc = noticias.get(2);
                    model.addAttribute("titul", notc.get("Title"));
                    model.addAttribute("lik", notc.get("Link"));
                    model.addAttribute("descr", notc.get("Description"));
                    model.addAttribute("fec", notc.get("Date"));

                    if(modo2.equals("tres")){
                        GestorTTS gestorTTS = GestorTTS.getGestorTTS();
                        gestorTTS.speak("titulo");gestorTTS.speak(notc.get("Title"));
                        gestorTTS.speak("link");gestorTTS.speak(notc.get("Link"));
                        gestorTTS.speak("descripcion");gestorTTS.speak(notc.get("Description"));
                        gestorTTS.speak("fecha");gestorTTS.speak(notc.get("Date"));
                    }
                }else{
                    String e ="";
                    model.addAttribute("titul",e);
                    model.addAttribute("lik",e);
                    model.addAttribute("descr",e);
                    model.addAttribute("fec",e);
                }
            }else{
                String e ="";
                model.addAttribute("titu",e);
                model.addAttribute("lin",e);
                model.addAttribute("des",e);
                model.addAttribute("fech",e);
                model.addAttribute("titul",e);
                model.addAttribute("lik",e);
                model.addAttribute("descr",e);
                model.addAttribute("fec",e);

            }
        }else{ //En caso de que no haya noticias disponibles
            String e ="";

            model.addAttribute("tit",e);
            model.addAttribute("link",e);
            model.addAttribute("desc",e);
            model.addAttribute("fecha",e);
            model.addAttribute("titu",e);
            model.addAttribute("lin",e);
            model.addAttribute("des",e);
            model.addAttribute("fech",e);
            model.addAttribute("titul",e);
            model.addAttribute("lik",e);
            model.addAttribute("descr",e);
            model.addAttribute("fec",e);

            if(!gestorSQLite.getListaServiciosActivos().contains("NewsDataIO")){
                String serv = "Error: Este servicio no está activado en el sistema.";
                model.addAttribute("mensaje",serv);
            }else{
                String serv = "Error: No hay noticias disponibles en estos momentos.";
                model.addAttribute("mensaje",serv);
            }

        }

        return "servicios/info";
    }
    //aa
    @RequestMapping("/servicios/info/{toponimo}")
    public String noticiaUbicacion(@PathVariable("toponimo") String toponimo,Model model){
        GestorNewsDataIO gestorNewsDataIO=GestorNewsDataIO.getGestorNewsDataIO();
        ArrayList<HashMap<String, String>> mapa=gestorNewsDataIO.peticion(toponimo);
        model.addAttribute("noticias",mapa);
        model.addAttribute("toponimo",toponimo);
        return "/servicios/infoUbic";
    }

    @RequestMapping("/servicios/eventos")
    public String eventos (Model model){

        GestorTicketMaster gestorTicketMaster = GestorTicketMaster.getGestorTicketmaster();
        ArrayList<HashMap<String, String>> eventos = gestorTicketMaster.peticion("");
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();

        if(eventos != null &&  2 < eventos.size() && gestorSQLite.getListaServiciosActivos().contains("TicketMaster")){

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

        }else{
            String mens = " ";
            model.addAttribute("name", mens);
            model.addAttribute("type",mens);
            model.addAttribute("location",mens);
            model.addAttribute("info",mens);

            model.addAttribute("nam",mens);
            model.addAttribute("typ",mens);
            model.addAttribute("locatio",mens);
            model.addAttribute("inf",mens);

            model.addAttribute("nom",mens);
            model.addAttribute("tip",mens);
            model.addAttribute("local",mens);
            model.addAttribute("infor",mens);

            if(!gestorSQLite.getListaServiciosActivos().contains("TicketMaster")){
                String serv = "Error: Este servicio no está activado en el sistema";
                model.addAttribute("mensaje",serv);
            }else{
                String serv = "Error: No hay eventos disponibles en este momento";
                model.addAttribute("mensaje",serv);
            }

        }

        return "servicios/eventos";
    }

    @RequestMapping(value="/servicios/list_event/{toponimo}")
    public String listEventos (@PathVariable("toponimo") String toponimo,Model model){

        GestorTicketMaster gestorTicketMaster = GestorTicketMaster.getGestorTicketmaster();
        ArrayList<HashMap<String, String>> eventos = gestorTicketMaster.peticion(toponimo);
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        String topo = gestorSQLite.formatearToponimo(toponimo);

        if(eventos != null &&  1 < eventos.size() && gestorSQLite.getListaServiciosActivos().contains("TicketMaster") && gestorSQLite.getListaUbicacionesActivas().contains(topo)) {

            /* Se muestran 2 eventos */
            HashMap<String, String> even1;
            HashMap<String, String> even2;
            even1 = eventos.get(0);
            even2 = eventos.get(1);


            model.addAttribute("name", even1.get("Event name"));
            model.addAttribute("type", even1.get("Type"));
            model.addAttribute("location", even1.get("Location"));
            model.addAttribute("info", even1.get("Information"));

            model.addAttribute("nam", even2.get("Event name"));
            model.addAttribute("typ", even2.get("Type"));
            model.addAttribute("locatio", even2.get("Location"));
            model.addAttribute("inf", even2.get("Information"));

            model.addAttribute("top", toponimo);
            System.out.println(eventos);

        }else{
            if(!gestorSQLite.getListaServiciosActivos().contains("TicketMaster")) {
                String mensajes = "Error: Este servicio no está activado en el sistema.";
                model.addAttribute("mensaje", mensajes);
                model.addAttribute("top", toponimo);
            }else{
                String mensajes = "Error: No hay eventos disponibles";
                model.addAttribute("mensaje", mensajes);
                model.addAttribute("top", toponimo);
            }
        }

        return "servicios/list_event";
    }


    @RequestMapping("/meteorologia")
    public String Meteorologia(Model model) {
        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();
        model.addAttribute("ubicacionesActivas", gestorSQLite.getListaUbicacionesActivas());

        return "meteorologia";
    }

    @RequestMapping("/meteorologiatopon")
    public String meteorolotop() {
        return "meteorologiatopon";
    }


    @RequestMapping("/servicios/ActivarServicio")
    public String activarServicio(@RequestParam(name="nombre",required = false,defaultValue ="") String servicio, Model model){

        GestorSQLite gestorSQLite =  GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        boolean activado = gestorSQLite.addServicioActivo(servicio);

        if(activado){
            String mens = "El servicio se ha activado correctamente";
            model.addAttribute("mensaje",mens);
        }else{
            if(gestorSQLite.getListaServiciosActivos().contains(servicio)){
                String mens = "Error: Este servicio ya está activo en el sistema";
                model.addAttribute("mensaje", mens);
            }
            if( !servicio.equals("") && !gestorSQLite.getListaServiciosDisponibles().contains(servicio)){
                String mens = "Error: Este servicio no está disponible en el sistema";
                model.addAttribute("mensaje", mens);
            }else{
                String mens = " ";
                model.addAttribute("mensaje", mens);
            }
        }

        return "servicios/ActivarServicio";
    }

    @RequestMapping("/servicios/desactivarServicio")
    public String desactivarServicio(@RequestParam(name="nombre",required = false,defaultValue ="") String servicio, Model model){

        GestorSQLite gestorSQLite = GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();

        if(!servicio.equals("") && gestorSQLite.getListaServiciosActivos().contains(servicio)){
            gestorSQLite.deleteServicioActivo(servicio);
            String mens = "El servicio se ha desactivado correctamente";
            model.addAttribute("mensaje",mens);
        }else{

            if( !servicio.equals("") && !gestorSQLite.getListaServiciosDisponibles().contains(servicio)){
                String mens = "Error: Este servicio no está disponible en el sistema";
                model.addAttribute("mensaje", mens);
            }else{
                String mens = " ";
                model.addAttribute("mensaje", mens);
            }
        }
        return "servicios/desactivarServicio";
    }

    @RequestMapping("/servicios/activar-servicio-ubic/{toponimo}")
    public String activarServUbic(@PathVariable("toponimo") String toponimo,
                                  @RequestParam(name="ubic",required = false,defaultValue ="") String ubic,
                                  @RequestParam(name="servicio",required = false,defaultValue ="") String servicio,
                                  Model model) {

        GestorSQLite gestorSQLite =  GestorSQLite.getGestorSQLite();
        gestorSQLite.connect();


        model.addAttribute("toponimo", toponimo);
        boolean activado = gestorSQLite.addServicioUbicacion(servicio, ubic);

        if(activado){
            String mens = "El servicio se ha activado correctamente";
            model.addAttribute("mensaje",mens);
        }else{
            if(gestorSQLite.getListaServiciosUbicacion().get(toponimo) != null && gestorSQLite.getListaServiciosUbicacion().get(toponimo).contains(servicio)) {
                    String mens = "Error: Este servicio ya está activo para esta ubicacion";
                    model.addAttribute("mensaje", mens);
            }else{



                if (!servicio.equals("") && !gestorSQLite.getListaServiciosDisponibles().contains(servicio)) {
                    String mens = "Error: Este servicio no está disponible en el sistema";
                    model.addAttribute("mensaje", mens);
                }

                if(!servicio.equals("") && !gestorSQLite.getListaServiciosActivos().contains(servicio)){
                    String mens = "Error: Este servicio no está activo en el sistema";
                    model.addAttribute("mensaje", mens);
                } else {
                    String mens = " ";
                    model.addAttribute("mensaje", mens);
                }
           }
        }

        return "servicios/activar-servicio-ubic";
    }

}
