package h23;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDesactivarServicio {

    private static GestorMain gestorMain;

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
    public void PAEscenario45(){
        //Given

        //When
        gestorMain.desactivarServicio("TicketMaster");

        //Then
        assertEquals(gestorMain.getListaServiciosActivos().size(),0);
        assertFalse(gestorMain.getListaServiciosActivos().contains("TicketMaster"));
    }

    @Test
    public void PAEscenario46(){
        //Given

        //When
        gestorMain.desactivarServicio("TicketMaster");

        //Then
        assertFalse(gestorMain.desactivarServicio("TicketMaster"));
        assertEquals(gestorMain.getListaServiciosActivos().size(),0);

    }
}
