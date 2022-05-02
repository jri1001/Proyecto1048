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

public class GestorNewsDataIO extends Servicio {
    private static GestorNewsDataIO gestorNewsDataIO;

    public GestorNewsDataIO() { }

    public static GestorNewsDataIO getGestorNewsDataIO(){
        if (gestorNewsDataIO==null){
            gestorNewsDataIO=new GestorNewsDataIO();
        }
        return gestorNewsDataIO;
    }

    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(
                str, new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        return map;
    }

    public ArrayList<HashMap<String, String>> peticion(String location) {  //cambiar lo que devuelve
        String API_KEY = "pub_1720d76ce70402502a760bd1c934f2ea0662";
        String myUrl = "https://newsdata.io/api/1/news?apikey=" + API_KEY;
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
            ArrayList<Map<String,Object>> listRes= (ArrayList) respMap.get("results");
            for(Map<String, Object> newsMap:listRes){
                HashMap<String,String> mapa = new HashMap<>();
                mapa.put("Title", newsMap.get("title").toString());
                mapa.put("Link", newsMap.get("link").toString());
                mapa.put("Description", newsMap.get("description").toString());
                mapa.put("Date", newsMap.get("pubDate").toString());
                resp.add(mapa);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resp;
    }
}
