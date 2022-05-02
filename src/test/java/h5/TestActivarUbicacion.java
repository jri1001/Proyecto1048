package h5;

import modelo.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestActivarUbicacion {

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
        gestorMain.addUbicacion("Valencia");
        gestorMain.syncDB();
    }


    @Test
    public void PAEscenarioE9(){


        //When
        gestorMain.activarUbicacion("Valencia");

        //Then
        List<String> lista=gestorMain.getListaUbicacionesActivas();
        assertEquals(lista.size(),1);
        assertEquals(lista.get(0),"valencia");
    }

    @Test
    public void PAEscenarioE10(){

        //Given
        String toponimo= "Valencia";

        //When
        gestorMain.activarUbicacion(toponimo);

        //Then
        assertFalse(gestorMain.activarUbicacion(toponimo));
        assertEquals(gestorMain.getListaUbicacionesActivas().size(),1);
    }

}