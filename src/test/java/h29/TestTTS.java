package h29;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestTTS {
    private static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
    }

    @BeforeEach
    public void setupTest(){
        gestorMain=new GestorMain();
    }

    @Test
    public void PAEscenario57(){
        //Given

        //When
        gestorMain.inject(GestorTTS.getGestorTTS());

        //Then
        assertTrue(gestorMain.verificarGestor("TTS"));
    }

    @Test
    public void PAEscenario58(){
        //Given

        //When

        //Then
        assertFalse(gestorMain.verificarGestor("TTS"));
    }
}
