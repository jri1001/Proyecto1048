package spike;

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

public class OpenWeather {

    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(
                str, new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        return map;
    }

    public static void main(String[] args) throws IOException {
        String API_KEY = "274e1792cb0eafe92c73ae399374342a";
        String LOCATION = "Valencia";
        String myUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + LOCATION + "&appid=" + API_KEY + "&lang={es}" + "&units=Metric";

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
            System.out.println("Open Weather Map API\n");

            Map<String, Object> respMap = jsonToMap(result.toString()); // Guarda toda la info del fichero JSON
            Map<String, Object> mainMap = jsonToMap(respMap.get("main").toString()); // Guarda la info del apartado main del fichero JSON
            ArrayList listWeather= (ArrayList) respMap.get("weather");
            Map<String, Object> weatherMap = (Map<String, Object>) listWeather.get(0);
            Map<String, Object> coordMap = jsonToMap(respMap.get("coord").toString());


            System.out.println("City: " + respMap.get("name").toString());
            System.out.println("Temperature: " + mainMap.get("temp"));
            System.out.println("Humidity: " + mainMap.get("humidity"));
            System.out.println("Weather: " + weatherMap.get("main"));
            System.out.println("Weather description: " + weatherMap.get("description"));
            System.out.println("Coord: \n\tlon: " + coordMap.get("lon") + "\n\tlat: " + coordMap.get("lat"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
