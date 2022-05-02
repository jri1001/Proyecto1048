package h14;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPeticionSimultaneaUbicaciones {
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
        gestorMain.addServicioUbicacion("TicketMaster",gestorMain.addUbicacion("Valencia").getNombre());
        gestorMain.addServicioUbicacion("TicketMaster",gestorMain.addUbicacion("Castelló de la Plana").getNombre());
        gestorMain.addServicioUbicacion("TicketMaster",gestorMain.addUbicacion("Benimaclet").getNombre());
        gestorMain.addServicioUbicacion("TicketMaster",gestorMain.addUbicacion("Gandia").getNombre());
        gestorMain.syncDB();

    }

    @Test
    public void PAEscenario27(){

        //Given
        ArrayList<String> lista=new ArrayList<>();
        lista.add("Valencia");
        lista.add("Benimaclet");
        lista.add("Gandia");
        ArrayList<String> lista2=new ArrayList<>();

        //When
        HashMap<String,HashMap<String, ArrayList<HashMap<String,String>>>> mapa2=gestorMain.peticionSimultaneaUbicaciones(lista2);
        HashMap<String,HashMap<String, ArrayList<HashMap<String,String>>>> mapa=gestorMain.peticionSimultaneaUbicaciones(lista);


        //Then
        assertTrue(mapa2.isEmpty());
        assertFalse(mapa.isEmpty());
        assertFalse(mapa.get("valencia").isEmpty() || mapa.get("benimaclet").isEmpty() || mapa.get("gandia").isEmpty());
    }

    @Test
    public void PAEscenario28(){

        //Given
        ArrayList<String> lista=new ArrayList<>();
        lista.add("Valencia");
        lista.add("Benimaclet");
        lista.add("Gandia");
        lista.add("Castelló de la Plana");

        //When
        HashMap<String,HashMap<String, ArrayList<HashMap<String,String>>>> mapa=gestorMain.peticionSimultaneaUbicaciones(lista);

        //Then
        assertTrue(mapa.isEmpty());
    }
}
