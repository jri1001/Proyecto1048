package h2;

import modelo.*;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreacionUbicacionCoordenadas {
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
    public void PAEscenario3(){
        //Given

        //When
        String[] coordenadas={gestorMain.formatearCoordenada("39.98920"), gestorMain.formatearCoordenada("-0.03620")}; //{latitud, longitud}, Castello de la Plana
        Ubicacion ubicacion=gestorMain.addUbicacion(coordenadas);;


        //Then
        assertEquals(gestorMain.getListaUbicaciones().keySet().size(),1);
        assertEquals(Double.parseDouble(ubicacion.getLatitud()),Double.parseDouble(coordenadas[0]), 0.0004); //las coordenadas encontradas por el geocoding pueden tener cierto margen de error
        assertEquals(Double.parseDouble(ubicacion.getLongitud()),Double.parseDouble(coordenadas[1]), 0.0004);
    }

    @Test
    public void PAEscenario4(){
        //Given

        //When
        String[] coordenadas={gestorMain.formatearCoordenada("12.13930"), gestorMain.formatearCoordenada("-86.30400")}; //{latitud, longitud}, Nicaragua
        Ubicacion ubicacion=gestorMain.addUbicacion(coordenadas);

        //Then
        assertEquals(gestorMain.getListaUbicaciones().keySet().size(),0);
    }
}
