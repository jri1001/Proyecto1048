package mockito_h1;

import modelo.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

public class PruebasIntegracion_ubic {

    @Mock
    static GestorMain gestorMain;


    @Before
    public void inicia() throws Exception {
        MockitoAnnotations.initMocks(this);

        gestorMain = new GestorMain();
        gestorMain.inject(GestorSQLite.getGestorSQLite());
        gestorMain.inject(GestorGeocoding.getGestorGeocoding());
        gestorMain.inject(GestorUbicacion.getGestorUbicacion());
        gestorMain.inject(GestorServicios.getGestorServicios());
        gestorMain.crearDB("test.db");
    }

    @Test
    public void integrityTest1() {
        String[] coordenadas = {gestorMain.formatearCoordenada("39.47071"),gestorMain.formatearCoordenada("-0.32998")};

        when(gestorMain.getCoordenadasUbicacion("Valencia")).thenReturn(coordenadas);

        String[] coordenadas2 = gestorMain.getCoordenadasUbicacion("Valencia");

        assertEquals(Double.parseDouble(coordenadas2[0]),Double.parseDouble(coordenadas[0]), 0.0004);
        assertEquals(Double.parseDouble(coordenadas2[1]),Double.parseDouble(coordenadas[1]), 0.0004);

    }

}
