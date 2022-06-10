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

public class GestorOpenWeather extends Servicio {
    private static GestorOpenWeather gestorOpenWeather;

    public GestorOpenWeather() { }

    public static GestorOpenWeather getGestorOpenWeather(){
        if (gestorOpenWeather==null){
            gestorOpenWeather=new GestorOpenWeather();
        }
        return gestorOpenWeather;
    }
    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(
                str, new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        return map;
    }

    public ArrayList<HashMap<String, String>> peticion(String location) {
        String API_KEY = "274e1792cb0eafe92c73ae399374342a";
        String myUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + API_KEY + "&units=Metric&lang=es";
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
            Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString()); // Guarda la info del apartado main del fichero JSON
            ArrayList<Map<String,Object>> listWeather= (ArrayList) respMap.get("weather");
            for(Map<String, Object> weatherMap:listWeather){
                HashMap<String,String> mapa=new HashMap<>();
                Map<String, Object> coordMap = jsonToMap(respMap.get("coord").toString());
                Map<String, Object> windMap = jsonToMap(respMap.get("wind").toString());

                mapa.put("City", respMap.get("name").toString());
                mapa.put("Temperature", mainMap.get("temp").toString());
                mapa.put("Humidity", mainMap.get("humidity").toString());
                mapa.put("Pressure", mainMap.get("pressure").toString());
                mapa.put("Velocidad aire", windMap.get("speed").toString());
                mapa.put("Weather", weatherMap.get("main").toString());
                mapa.put("Weather description", weatherMap.get("description").toString());
                mapa.put("Altitud", coordMap.get("lon").toString());
                mapa.put("Latitud", coordMap.get("lat").toString());
                resp.add(mapa);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resp;
    }
}
