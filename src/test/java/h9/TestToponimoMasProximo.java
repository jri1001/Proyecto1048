package h9;

import modelo.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestToponimoMasProximo {

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


    @Test
    public void PAEscenarioE17() {

        //Given
        String[] coordenadas={gestorMain.formatearCoordenada("39.98920"), gestorMain.formatearCoordenada("-0.03620")}; //{latitud, longitud}, Castello de la Plana

        //When
        String toponimo=gestorMain.getToponimoCoordenadas(coordenadas);

        //Then
        assertNotNull(toponimo);
        assertEquals(toponimo,"castelló de la plana");
    }

    @Test
    public void PAEscenarioE18() {

        //Given
        String[] coordenadas={gestorMain.formatearCoordenada("12.13930"), gestorMain.formatearCoordenada("-86.30400")}; //{latitud, longitud}, Nicaragua

        //When
        String toponimo=gestorMain.getToponimoCoordenadas(coordenadas);

        //Then
        assertNull(toponimo);
    }
}
