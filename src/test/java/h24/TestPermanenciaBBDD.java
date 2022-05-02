package h24;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPermanenciaBBDD {
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
        gestorMain.activarServicio("TicketMaster");
        gestorMain.syncDB();
    }

    @Test
    public void PAEscenario47(){
        //Given
        List<String> lista=gestorMain.getListaServiciosActivos();

        //When
        gestorMain.crearDB("test2.db");
        gestorMain.syncDB();
        gestorMain.activarServicio("OpenWeather");
        gestorMain.cambiarDB("test.db");
        gestorMain.syncDB();

        //Then
        assertEquals(gestorMain.getListaServiciosActivos(),lista);
    }

    @Test
    public void PAEscenario48(){
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
