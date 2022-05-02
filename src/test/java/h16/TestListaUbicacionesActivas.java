package h16;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestListaUbicacionesActivas {

    private static GestorMain gestorMain;

    @BeforeAll
    static void setup(){
        gestorMain=new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.crearDB("test.db");
        gestorMain.addUbicacion("valencia");
        gestorMain.activarUbicacion("valencia");
        gestorMain.syncDB();

    }


    @Test
    public void PAEscenarioE31(){
        //Given

        //When
        List<String> list=gestorMain.getListaUbicacionesActivas();
        //Then
        assertEquals(list.size(),1);
        assertEquals(list.get(0),"valencia");
    }

    @Test
    public void PAEscenarioE32(){

        //Given

        //When
        gestorMain.desactivarUbicacion("valencia");
        List<String> list=gestorMain.getListaUbicacionesActivas();
        //Then
        assertEquals(list.size(),0);
    }

}
