package h26;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestPermanenciaBBDDCorte {
    private static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.crearDB("test2.db");
    }

    @BeforeEach
    public void setupTest(){
        gestorMain.crearDB("test.db");
        gestorMain.addUbicacion("valencia");
        gestorMain.syncDB();
    }

    @Test
    public void PAEscenario51(){
        //Given
        Map<String,Ubicacion> lista=gestorMain.getListaUbicaciones();

        //When
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.cambiarDB("test.db");
        gestorMain.syncDB();

        //Then
        assertEquals(gestorMain.getListaUbicaciones().toString(),lista.toString());
    }

    @Test
    public void PAEscenario52(){
        //Given
        String lista=gestorMain.getListaUbicaciones().toString();

        //When
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.cambiarDB("test2.db");
        gestorMain.syncDB();

        //Then
        assertNotEquals(gestorMain.getListaUbicaciones().toString(),lista);
    }
}
