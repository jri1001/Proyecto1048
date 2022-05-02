package modelo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public interface InterfaceAPI {
    static Map<String, Object> jsonToMap(String str) {
        return new Gson().fromJson(
                str, new TypeToken<HashMap<String, Object>>() {}.getType()
        );
    }

    void peticion(String location);

}
