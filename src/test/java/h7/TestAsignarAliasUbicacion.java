package h7;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestAsignarAliasUbicacion {
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
        gestorMain.syncDB();
    }


    @Test
    public void PAEscenario13(){

        //Given

        //When
        gestorMain.addAlias("Valencia","VLC");

        //Then
        List<String> lista=gestorMain.getListaAlias("Valencia");
        assertEquals(lista.size(),1);
        assertEquals(lista.get(0),"VLC");
    }

    @Test
    public void PAEscenario14() {

        //Given
        gestorMain.addAlias("Valencia","VLC");

        //When
        boolean bool=gestorMain.addAlias("Valencia","VLC");

        //Then
        assertFalse(bool);
        assertEquals(gestorMain.getListaAlias("Valencia").size(),1);
    }
}
