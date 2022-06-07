package h22;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestInfoServicios {

    static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.crearDB("test.db");
    }

    @Test
    public void PAEscenario43(){
        //Given

        //When
        HashMap<String, String> lista=gestorMain.getinfoServicios();

        //Then
        assertEquals(lista.size(),4);
        assertNotNull(lista.get("TicketMaster"));
    }

    @Test
    public void PAEscenario44(){
        ///Given

        //When
        HashMap<String, String> lista=gestorMain.getinfoServicios();

        //Then
        assertEquals(lista.size(),4);
        assertNull(lista.get("Deportes"));
    }
}
