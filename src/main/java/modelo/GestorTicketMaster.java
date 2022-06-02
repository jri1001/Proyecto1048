package modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GestorTicketMaster extends Servicio {
    private static GestorTicketMaster gestorTicketmaster;

    public GestorTicketMaster() { }

    public static GestorTicketMaster getGestorTicketmaster(){
        if (gestorTicketmaster==null){
            gestorTicketmaster=new GestorTicketMaster();
        }
        return gestorTicketmaster;
    }

    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(
                str, new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        return map;
    }

    public ArrayList<HashMap<String, String>> peticion(String location) {
        String API_KEY = "FKM66NQuNZ4k6GAAEJWl57l2tYDQ7VTA";
        String myUrl = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=ES&city=" + location + "&apikey=" + API_KEY + "&locale=es";
        ArrayList<HashMap<String, String>> resp = new ArrayList<>();

        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(myUrl);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            rd.close();

            Map<String, Object> respMap = jsonToMap(result.toString()); // Guarda toda la info del fichero JSON
            Map<String, Object> embMap = (Map<String, Object>) respMap.get("_embedded");
            if(embMap!=null){
                ArrayList<Map<String,Object>> listRes= (ArrayList) embMap.get("events");
                for(Map<String, Object> eventsMap:listRes){
                    HashMap<String,String> mapa=new HashMap<>();
                    mapa.put("Event name", eventsMap.get("name").toString());
                    mapa.put("Type", eventsMap.get("type").toString());
                    mapa.put("Location", eventsMap.get("locale").toString());
                    if(eventsMap.get("info") != null)
                        mapa.put("Information", eventsMap.get("info").toString());
                    else mapa.put("Information", "No hay información de momento.");
                    resp.add(mapa);
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resp;
    }
}
