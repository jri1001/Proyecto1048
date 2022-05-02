package h18;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestHistorialUbicaciones {

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
    void setupTest(){
        gestorMain.clearUbicaciones();
        gestorMain.addUbicacion("Valencia");
        gestorMain.addUbicacion("gandia");
        gestorMain.activarUbicacion("valencia");
    }

    @Test
    public void PAEscenario35(){
        //Given

        //When
        Map<String,Ubicacion> mapa= gestorMain.getListaUbicaciones();

        //Then
        assertEquals(mapa.size(),2);
        assertNotNull(mapa.get("valencia"));
        assertNotNull(mapa.get("gandia"));
        assertFalse(gestorMain.getListaUbicacionesActivas().contains("gandia"));
    }

    @Test
    public void PAEscenario36(){
        //Given

        //When
        gestorMain.deleteUbicacion("valencia");
        gestorMain.deleteUbicacion("gandia");
        Map<String,Ubicacion> mapa= gestorMain.getListaUbicaciones();

        //Then
        assertEquals(mapa.size(),0);
    }
}
