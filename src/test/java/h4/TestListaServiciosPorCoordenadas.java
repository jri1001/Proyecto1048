package h4;

import modelo.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestListaServiciosPorCoordenadas {

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
    public void PAEscenario7(){

        //Given

        //When
        HashMap<String, ArrayList<HashMap<String, String>>> mapa=gestorMain.infoUbicacionServicios(gestorMain.getCoordenadasUbicacion("Valencia"));

        //Then
        assertNotNull(mapa.get("TicketMaster"));
    }

    @Test
    public void PAEscenario8(){

        //Given

        //When
        HashMap<String, ArrayList<HashMap<String, String>>> mapa=gestorMain.infoUbicacionServicios(new String[]{"-38.416097","-63.616672"}); //argentina

        //Then
        assertNull(mapa.get("TicketMaster"));
    }
}


