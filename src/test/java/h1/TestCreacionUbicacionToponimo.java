package h1;

import modelo.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreacionUbicacionToponimo {
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
        gestorMain.syncDB();
    }

    @Test
    public void PAEscenario1(){
        //Given

        //When
        String toponimo="Valencia";
        gestorMain.addUbicacion(toponimo);
        toponimo="Benimaclet";
        gestorMain.addUbicacion(toponimo);


        //Then
        assertEquals(gestorMain.getListaUbicaciones().keySet().size(),2);
        assertEquals(gestorMain.getListaUbicaciones().get("valencia").getNombre(),"valencia");
        assertEquals(gestorMain.getListaUbicaciones().get("benimaclet").getNombre(),"benimaclet");
    }

    @Test
    public void PAEscenario2(){
        //Given

        //When
        String toponimo="Argentina"; //no se puede salir de españa
        gestorMain.addUbicacion(toponimo);

        //Then
        assertEquals(gestorMain.getListaUbicaciones().keySet().size(),0);
    }
}
