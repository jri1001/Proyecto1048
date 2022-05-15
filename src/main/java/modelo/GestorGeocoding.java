package modelo;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class GestorGeocoding implements IntGestorGeocoding{
    private static GestorGeocoding gestorGeocoding;

    public GestorGeocoding() { }

    public static GestorGeocoding getGestorGeocoding(){
        if (gestorGeocoding==null){
            gestorGeocoding=new GestorGeocoding();
        }
        return gestorGeocoding;
    }

    public static Map<String, Object> jsonToMap(String str2) {
        JsonParser jsonParser=new JsonParser();
        JsonObject jsonObject=jsonParser.parse(str2).getAsJsonObject();
        Map<String, Object> map = toMap(jsonObject);


        //Map<String, Object> map = new Gson().fromJson(str2.trim(), new TypeToken<HashMap<String, Object>>() {}.getType());
        return map;
    }

    public static Map<String, Object> toMap(JsonObject object) {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keySet().iterator();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JsonArray) {
                value = toList((JsonArray) value);
            }
            else if(value instanceof JsonObject) {
                value = toMap((JsonObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JsonArray array) {
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if(value instanceof JsonArray) {
                value = toList((JsonArray) value);
            }

            else if(value instanceof JsonObject) {
                value = toMap((JsonObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public Ubicacion peticion(String toponimo){
        Ubicacion ubicacion=new Ubicacion();
        String myUrl="http://geocode.xyz/"+toponimo+"?json=1&region=ES";
        Map<String, Object> respMap = conexion(myUrl);

        if(respMap==null || respMap.get("error")!=null){ return null;}
        Map<String, Object> standard;
        if(respMap.get("osmtags") == null){
            standard=jsonToMap(respMap.get("standard").toString());

            if(!respMap.get("alt").toString().equals("{}")) {
                //ubicacion.setNombre(standard.get("city").toString());
                Map<String, Object> alt = (Map<String, Object>) respMap.get("alt");
                try{
                    ArrayList loc = (ArrayList) alt.get("loc");
                    standard = (Map<String, Object>) loc.get(0);
                }catch(ClassCastException e){
                    HashMap<String,Object> loc = (HashMap<String, Object>) alt.get("loc");
                    standard = loc;
                }
                ubicacion.setProvincia(standard.get("prov").toString());
                ubicacion.setLongitud(standard.get("longt").toString());
                ubicacion.setLatitud(standard.get("latt").toString());
            } else{
                ubicacion.setProvincia(standard.get("countryname").toString());
                ubicacion.setLongitud(respMap.get("longt").toString());
                ubicacion.setLatitud(respMap.get("latt").toString());
            }
            ubicacion.setNombre(formatearToponimo(standard.get("city").toString()));
            ubicacion.setCiudad(standard.get("city").toString());
            ubicacion.setCod_postal(standard.get("postal").toString());
        } else{
            standard=(Map<String, Object>) respMap.get("osmtags");
            if(!respMap.get("prov").toString().replaceAll("\"","").equals("ES")){
                return null;
            }
            ubicacion.setNombre(formatearToponimo(standard.get("name").toString()));
            ubicacion.setCiudad(respMap.get("city").toString());
            ubicacion.setProvincia(respMap.get("state").toString());
            ubicacion.setCod_postal(respMap.get("postal").toString());
            ubicacion.setLongitud(respMap.get("longt").toString());
            ubicacion.setLatitud(respMap.get("latt").toString());
        }
        return ubicacion;
    }

    private Map<String, Object> conexion(String myUrl){
        StringBuilder result=new StringBuilder();
        int reintentos=0;
        while(reintentos<20) {
            try {
                URL url = new URL(myUrl);
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("UserAgent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

                InputStream is;
                conn.connect();
                is=conn.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                rd.close();
                break;
            } catch (IOException e) {
                reintentos++;
                if(reintentos==20){return null;}
            }
        }
        return jsonToMap(result.toString());
    }
    private String formatearToponimo(String toponimo){
        return toponimo.toLowerCase(Locale.ROOT); //todos los toponimos en minuscula
    }
}
