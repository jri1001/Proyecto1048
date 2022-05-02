package h21;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestActivarServicio {


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
    }

    @Test
    public void PAEscenario41(){
        //Given

        //When
        gestorMain.activarServicio(gestorMain.getListaServicios().get(0));

        //Then
        assertEquals(gestorMain.getListaServiciosActivos().size(),1);
    }

    @Test
    public void PAEscenario42(){
        //Given

        //When
        gestorMain.activarServicio("Deporte"); // Deporte es un servicio no valido

        //Then
        assertFalse(gestorMain.getListaServicios().contains("Deporte"));
        assertEquals(gestorMain.getListaServiciosActivos().size(),0);

    }
}
