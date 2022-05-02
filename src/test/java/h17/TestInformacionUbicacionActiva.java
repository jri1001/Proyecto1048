package h17;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestInformacionUbicacionActiva {

    static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.crearDB("test.db");
        gestorMain.addUbicacion("Valencia");
        gestorMain.addUbicacion("gandia");
        gestorMain.activarServicio("TicketMaster");
        gestorMain.activarServicio("OpenWeather");
        gestorMain.syncDB();
    }

    @BeforeEach
    public void setupTest(){
        gestorMain.activarUbicacion("Valencia");
        gestorMain.activarUbicacion("gandia");
        gestorMain.addServicioUbicacion("TicketMaster","valencia");
        gestorMain.addServicioUbicacion("OpenWeather","gandia");
    }

    @Test
    public void PAEscenario33(){
        //Given

        //When
        HashMap<String, ArrayList<HashMap<String,String>>> mapa=gestorMain.infoUbicacionActivaServicios("valencia");

        //Then
        assertFalse(mapa.isEmpty());
        assertNotEquals(mapa.toString(),gestorMain.infoUbicacionActivaServicios("gandia").toString());
    }

    @Test
    public void PAEscenario34(){
        //Given

        //When
        gestorMain.desactivarUbicacion("gandia");
        HashMap<String, ArrayList<HashMap<String,String>>> mapa=gestorMain.infoUbicacionActivaServicios("castello de la plana");

        //Then
        assertTrue(mapa.isEmpty());
    }

}
