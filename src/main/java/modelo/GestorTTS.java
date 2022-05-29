package modelo;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class GestorTTS implements IntGestorTTS{
    private static GestorTTS gestorTTS;
    private Voice voice;

    public GestorTTS() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        this.voice= VoiceManager.getInstance().getVoice("kevin");
    }

    public static GestorTTS getGestorTTS(){
        if (gestorTTS==null){
            gestorTTS=new GestorTTS();
        }
        return gestorTTS;
    }

    public void speak(String texto) {
        voice.allocate();
        voice.speak(texto);
        //voice.deallocate(); //Esta comentado porque sino solo deja hacer una peticion de speak
    }
}
