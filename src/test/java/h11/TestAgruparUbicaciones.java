package h11;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestAgruparUbicaciones {

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
        gestorMain.addUbicacion("valencia");
    }

    @Test
    public void PAEscenario21(){
        //Given
        List<String> ubicaciones = new ArrayList<>();
        ubicaciones.add("Castellon de la Plana");
        ubicaciones.add("Valencia");

        //When


        //Then
        assertTrue(gestorMain.crearGrupoUbic(ubicaciones));
    }

    @Test
    public void PAEscenario22(){
        //Given
        List<String> ubicaciones = new ArrayList<>();
        ubicaciones.add("Valencia");
        // When


        //Then
        assertFalse(gestorMain.crearGrupoUbic(ubicaciones));
    }
}
