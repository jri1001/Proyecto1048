package h20;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestConsultaServiciosDisponibles {

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
    public void setupTest(){
        gestorMain.clearServicios();
        gestorMain.activarServicio("TicketMaster");
    }

    @Test
    public void PAEscenario39(){
        //Given

        //When
        List<String> lista=gestorMain.getListaServicios();

        //Then
        assertEquals(lista.size(),3);
        assertTrue(lista.contains("TicketMaster"));
        assertTrue(lista.contains("OpenWeather"));
    }

    @Test
    public void PAEscenario40(){
        //Given

        //When
        gestorMain.desactivarServicio("TicketMaster");
        List<String> lista=gestorMain.getListaServicios();

        //Then
        assertEquals(lista.size(),3);
        assertTrue(lista.contains("TicketMaster"));
        assertTrue(lista.contains("OpenWeather"));
    }
}
