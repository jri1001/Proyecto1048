package h3;

import modelo.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestListaServiciosPorToponimo {
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

    @BeforeEach
    void setupTest(){
        gestorMain.clearUbicaciones();
        gestorMain.addUbicacion("Valencia");
        gestorMain.activarServicio("TicketMaster");
    }

    @Test
    public void PAEscenario5(){

        //Given

        //When
        HashMap<String, ArrayList<HashMap<String, String>>> mapa=gestorMain.infoUbicacionServicios("Valencia");

        //Then
        assertNotNull(mapa.get("TicketMaster"));
    }

    @Test
    public void PAEscenario6(){

        //Given

        //When
        HashMap<String, ArrayList<HashMap<String, String>>> mapa=gestorMain.infoUbicacionServicios("Nicaragua");

        //Then
        assertNull(mapa.get("TicketMaster"));
    }
}