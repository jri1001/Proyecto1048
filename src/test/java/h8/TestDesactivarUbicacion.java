package h8;

import modelo.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestDesactivarUbicacion {

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
    void setupTest(){
        gestorMain.clearUbicaciones();
        String toponimo="Valencia";
        gestorMain.addUbicacion(toponimo);
        gestorMain.activarUbicacion(toponimo);
        gestorMain.syncDB();
    }

    @Test
    public void PAEscenarioE15() {

        //Given

        //When
        String toponimo = "Valencia";
        boolean b =gestorMain.desactivarUbicacion(toponimo);

        //Then
        assertTrue(b);
        assertEquals(gestorMain.getListaUbicacionesActivas().size(), 0);

    }

    @Test
    public void PAEscenarioE16() {

        //Given

        //When
        String toponimo = "Valencia";
        gestorMain.desactivarUbicacion(toponimo);
        boolean b =gestorMain.desactivarUbicacion(toponimo);

        //Then
        assertFalse(b);
    }
}