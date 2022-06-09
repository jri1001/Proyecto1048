package h27;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestImportarBBDD {
    private static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.crearDB("test2.db");
        gestorMain.addUbicacion("gandia");
    }

    @BeforeEach
    public void setupTest(){
        gestorMain.crearDB("test.db");
        gestorMain.syncDB();
    }

    @Test
    public void PAEscenario53(){
        //Given
        Map<String,Ubicacion> lista=gestorMain.getListaUbicaciones();

        //When
        gestorMain.cambiarDB("test2.db");
        gestorMain.syncDB();

        //Then
        assertNotEquals(gestorMain.getListaUbicaciones().toString(),lista.toString());
    }

    @Test
    public void PAEscenario54(){
        //Given

        //When
        boolean b=gestorMain.cambiarDB("test2");

        //Then
        assertFalse(b);
    }
}
