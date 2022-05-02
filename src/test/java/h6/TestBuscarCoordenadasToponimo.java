package h6;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBuscarCoordenadasToponimo {
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
    public void PAEscenario11(){
        //Given
        String toponimo="Valencia";
        String[] coordenadas={gestorMain.formatearCoordenada("39.47071"),gestorMain.formatearCoordenada("-0.32998")};//coordenadas devueltas por http://geocode.xyz/Valencia?json=1&region=ES

        //When
        String[] coordenadas2=gestorMain.getCoordenadasUbicacion(toponimo);

        //Then
        assertEquals(Double.parseDouble(coordenadas2[0]),Double.parseDouble(coordenadas[0]), 0.0004); //las coordenadas encontradas por el geocoding pueden tener cierto margen de error
        assertEquals(Double.parseDouble(coordenadas2[1]),Double.parseDouble(coordenadas[1]), 0.0004);
    }

    @Test
    public void PAEscenario12(){
        //Given
        String toponimo="Argentina";

        //When
        String[] coordenadas2=gestorMain.getCoordenadasUbicacion(toponimo);

        //Then
        assertNull(coordenadas2);
    }
}
