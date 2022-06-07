package h19;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUbicacionesRecientes {

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
        gestorMain.clearUbicaciones();
        gestorMain.activarUbicacion(gestorMain.addUbicacion("Valencia").getNombre());
        gestorMain.activarUbicacion(gestorMain.addUbicacion("gandia").getNombre());
    }


    @Test
    public void PAEscenarioE37(){

        //Given

        //When
        gestorMain.getUbicacion("valencia");
        gestorMain.getUbicacion("gandia");
        gestorMain.getUbicacion("valencia");
        HashSet<String> lista=gestorMain.getListaUbicacionesRecientes();

        //Then
        assertEquals(lista.size(),2);
        assertTrue(lista.contains("valencia"));
        assertTrue(lista.contains("gandia"));
    }

    @Test
    public void PAEscenarioE38(){

        //Given

        //When
        gestorMain.getUbicacion("valencia");
        gestorMain.deleteUbicacion("valencia");
        HashSet<String> lista=gestorMain.getListaUbicacionesRecientes();

        //Then
        assertEquals(lista.size(),0);
    }
}
