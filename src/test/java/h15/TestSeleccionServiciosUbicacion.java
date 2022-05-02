package h15;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestSeleccionServiciosUbicacion {
    static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.crearDB("test.db");
        gestorMain.activarServicio("TicketMaster");
        gestorMain.activarServicio("OpenWeather");
        gestorMain.syncDB();
    }

    @BeforeEach
    public void setupTest(){
        gestorMain.clearUbicaciones();
        gestorMain.addUbicacion("Valencia");
        gestorMain.addUbicacion("Castello de la Plana");
    }

    @Test
    public void PAEscenario29(){

        //Given
        ArrayList<String> lista=new ArrayList<>();
        lista.add("valencia");
        lista.add("castello de la plana");

        //When
        gestorMain.addServicioUbicacion("TicketMaster", "valencia");
        gestorMain.addServicioUbicacion("OpenWeather", "castello de la plana");
        HashMap<String,HashMap<String, ArrayList<HashMap<String,String>>>> mapa=gestorMain.peticionSimultaneaUbicaciones(lista);

        //Then
        assertFalse(mapa.get("valencia").containsKey("OpenWeather") && mapa.get("castello de la plana").containsKey("TicketMaster"));
        assertTrue(mapa.get("valencia").containsKey("TicketMaster") && mapa.get("castello de la plana").containsKey("OpenWeather"));
    }

    @Test
    public void PAEscenario30(){

        //Given
        ArrayList<String> lista=new ArrayList<>();
        lista.add("Valencia");
        lista.add("Gandia");

        //When
        gestorMain.addServicioUbicacion("TicketMaster", "Valencia");
        gestorMain.addServicioUbicacion("OpenWeather", "gandia");
        HashMap<String,HashMap<String, ArrayList<HashMap<String,String>>>> mapa=gestorMain.peticionSimultaneaUbicaciones(lista);


        //Then
        assertFalse(mapa.get("valencia").isEmpty());
        assertNull(mapa.get("gandia"));
    }
}
