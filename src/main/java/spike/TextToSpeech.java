package spike;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TextToSpeech {
    public static Map<String, Object> jsonToMap(String str) {
        Map<String, Object> map = new Gson().fromJson(
                str, new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        return map;
    }

    public static void main(String[] args) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice= VoiceManager.getInstance().getVoice("kevin");
        voice.allocate();

        String[] listaPeticiones=new String[2];
        String MyUrl;
        String peticion1="Valencia";
        String peticion2="39.4702,-0.376805";

        String MyUrl1="http://geocode.xyz/"+peticion1+"?json=1&region=ES";
        String MyUrl2="http://geocode.xyz/"+peticion2+"?json=1&region=ES";
        listaPeticiones[0]=MyUrl1;
        listaPeticiones[1]=MyUrl2;

        System.out.println("\nGeocode.xyz API Test");
        StringBuilder result;
        for (String peticion : listaPeticiones) {

            System.out.println("\nPeticion: "+peticion);

            result = new StringBuilder();
            MyUrl = peticion;
            int reintentos=0;
            while(reintentos<20){
                try{
                    URL url = new URL(MyUrl);
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
                }
            }

            Map<String, Object> respMap = jsonToMap(result.toString());
            //if(respMap.get("success")!=null){System.out.println("timeout");}

            if(respMap.get("osmtags") == null){
                Map<String, Object> standard=jsonToMap(respMap.get("standard").toString());
                if(!respMap.get("alt").toString().equals("")){
                    Map<String, Object> alt=(Map<String, Object>)respMap.get("alt");
                    ArrayList loc= (ArrayList) alt.get("loc");
                    Map<String, Object> localizacion=(Map<String, Object>) loc.get(0);
                    System.out.println("Nombre: "+localizacion.get("city").toString());
                    voice.speak("Nombre: "+localizacion.get("city").toString());
                    System.out.println("Provincia: "+localizacion.get("prov").toString());
                    voice.speak("Provincia: "+localizacion.get("prov").toString());
                    System.out.println("Codigo postal: "+localizacion.get("postal").toString());
                    voice.speak("Codigo postal: "+localizacion.get("postal").toString());
                    System.out.println("Longitud: "+localizacion.get("longt").toString());
                    voice.speak("Longitud: "+localizacion.get("longt").toString());
                    System.out.println("Latitud: "+localizacion.get("latt").toString());
                    voice.speak("Latitud: "+localizacion.get("latt").toString());
                } else{
                    System.out.println("Nombre: "+standard.get("city").toString());
                    voice.speak("Nombre: "+standard.get("city").toString());
                    System.out.println("Provincia: "+standard.get("countryname").toString());
                    voice.speak("Provincia: "+standard.get("countryname").toString());
                    System.out.println("Codigo postal: "+standard.get("postal").toString());
                    voice.speak("Codigo postal: "+standard.get("postal").toString());
                    System.out.println("Longitud: "+respMap.get("longt").toString());
                    voice.speak("Longitud: "+respMap.get("longt").toString());
                    System.out.println("Latitud: "+respMap.get("latt").toString());
                    voice.speak("Latitud: "+respMap.get("latt").toString());
                }
            } else{
                Map<String, Object> osmtags=(Map<String, Object>) respMap.get("osmtags");
                System.out.println("Nombre: "+osmtags.get("name").toString());
                voice.speak("Nombre: "+osmtags.get("name").toString());
                System.out.println("Ciudad: "+respMap.get("city").toString());
                voice.speak("Ciudad: "+respMap.get("city").toString());
                System.out.println("Provincia: "+respMap.get("state").toString());
                voice.speak("Provincia: "+respMap.get("state").toString());
                System.out.println("Codigo postal: "+respMap.get("postal").toString());
                voice.speak("Codigo postal: "+respMap.get("postal").toString());
                System.out.println("Longitud: "+respMap.get("longt").toString());
                voice.speak("Longitud: "+respMap.get("longt").toString());
                System.out.println("Latitud: "+respMap.get("latt").toString());
                voice.speak("Latitud: "+respMap.get("latt").toString());
            }
        }
        voice.deallocate();
    }
}