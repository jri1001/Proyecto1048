package h12;

import modelo.GestorMain;
import modelo.GestorSQLite;
import modelo.GestorUbicacion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestEliminarGrupo {

    static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
    }

    @BeforeEach
    void setupTest(){
        gestorMain.clearUbicaciones();
    }

    @Test
    public void PAEscenario23(){
        //Given
        List<String> ubicaciones = new ArrayList<>();
        ubicaciones.add("Castellon de la Plana");
        ubicaciones.add("Valencia");
        gestorMain.crearGrupoUbic(ubicaciones);
        //When


        //Then
        assertTrue(gestorMain.eliminarGrupo(ubicaciones));
    }

    @Test
    public void PAEscenario24(){
        //Given
        List<String> ubicaciones = new ArrayList<>();
        // When


        //Then
        assertFalse(gestorMain.eliminarGrupo(ubicaciones));
    }
}
