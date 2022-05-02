package h10;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBorrarUbicacion {

    static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.crearDB("test.db");;
    }

    @BeforeEach
    void setupTest(){
        gestorMain.clearUbicaciones();
        String toponimo="Valencia";
        gestorMain.addUbicacion(toponimo);
        gestorMain.syncDB();
    }

    @Test
    public void PAEscenario19(){
        //Given

        //When
        boolean b=gestorMain.deleteUbicacion("Valencia");

        //Then
        assertTrue(b);
        assertEquals(gestorMain.getListaUbicaciones().keySet().size(),0);
    }

    @Test
    public void PAEscenario20(){
        //Given

        // When
        boolean b=gestorMain.deleteUbicacion("Castelló de la Plana");

        //Then
        assertFalse(b);
        assertEquals(gestorMain.getListaUbicaciones().keySet().size(),1);
    }
}
