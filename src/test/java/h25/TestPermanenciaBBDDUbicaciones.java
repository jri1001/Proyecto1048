package h25;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestPermanenciaBBDDUbicaciones {
    private static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
    }

    @BeforeEach
    public void setupTest(){
        gestorMain.crearDB("test.db");
        gestorMain.addUbicacion("valencia");
        gestorMain.syncDB();
    }

    @Test
    public void PAEscenario49(){
        //Given
        Map<String,Ubicacion> lista=gestorMain.getListaUbicaciones();

        //When
        gestorMain.crearDB("test2.db");
        gestorMain.syncDB();
        gestorMain.addUbicacion("gandia");
        gestorMain.cambiarDB("test.db");
        gestorMain.syncDB();

        //Then
        assertEquals(gestorMain.getListaUbicaciones().toString(),lista.toString());
    }

    @Test
    public void PAEscenario50(){
        //Given
        List<String> lista=gestorMain.getListaServiciosActivos();

        //When
        gestorMain.crearDB("test2.db");
        gestorMain.syncDB();
        gestorMain.activarServicio("OpenWeather");
        gestorMain.syncDB();

        //Then
        assertNotEquals(gestorMain.getListaServiciosActivos(),lista);
    }
}
