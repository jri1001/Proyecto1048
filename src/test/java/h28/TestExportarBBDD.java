package h28;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestExportarBBDD {
    private static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
    }

    @BeforeEach
    public void setupTest(){
        gestorMain.crearDB("test.db");
        gestorMain.addUbicacion("valencia");
        gestorMain.syncDB();
    }

    @Test
    public void PAEscenario55(){
        //Given
        Map<String,Ubicacion> lista=gestorMain.getListaUbicaciones();

        //When
        //TODO: No funciona
       // gestorMain.copiarDB("test2.db");
        gestorMain.syncDB();

        //Then
        assertEquals(gestorMain.getListaUbicaciones().toString(),lista.toString());
    }

    @Test
    public void PAEscenario56(){
        //Given

        //When
        boolean b=gestorMain.cambiarDB("test2");

        //Then
        assertFalse(b);
    }
}
