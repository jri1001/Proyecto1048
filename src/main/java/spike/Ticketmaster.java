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

public class Ticketmaster {

    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(
                str, new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        return map;
    }

    public static void main(String[] args) throws IOException {

        String API_KEY = "FKM66NQuNZ4k6GAAEJWl57l2tYDQ7VTA";
        String LOCATION = "US";
        String myUrl = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=" + LOCATION + "&apikey=" + API_KEY;

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
            System.out.println("Ticketmaster API:");

            Map<String, Object> respMap = jsonToMap(result.toString()); // Guarda toda la info del fichero JSON
            Map<String, Object> embMap = (Map<String, Object>) respMap.get("_embedded");
            ArrayList listRes= (ArrayList) embMap.get("events");
            Map<String, Object> eventsMap=(Map<String, Object>) listRes.get(1);

            System.out.println("Name: "+ eventsMap.get("name"));
            System.out.println("Type: "+ eventsMap.get("type"));
            System.out.println("Location: "+ eventsMap.get("locale"));

            if(eventsMap.get("info") != null)
                System.out.println("Information: "+ eventsMap.get("info"));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
