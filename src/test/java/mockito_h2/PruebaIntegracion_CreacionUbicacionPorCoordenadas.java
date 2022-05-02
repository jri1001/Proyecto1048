package mockito_h2;

import modelo.*;
import org.junit.Test;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PruebaIntegracion_CreacionUbicacionPorCoordenadas {

    GestorGeocoding gestorGeocoding = new GestorGeocoding();
    GestorUbicacion gestorUbicacion = new GestorUbicacion();
    GestorSQLite gestorSQLite = new GestorSQLite();
    GestorServicios gestorServicios = new GestorServicios();

    @Test
    public void  CreacionUbicacion_escValido(){

        IntGestorMain pDatosMock = mock(IntGestorMain.class);

        GestorMain t = new GestorMain();

        t.inject(gestorGeocoding);
        t.inject(gestorUbicacion);
        t.inject(gestorSQLite);
        t.inject(gestorServicios);

        boolean resultado = t.clearUbicaciones();
        t.syncDB();
        String[] coordenadas={t.formatearCoordenada("39.98920"), t.formatearCoordenada("-0.03620")};
        t.addUbicacion(coordenadas);
        Map<String, Ubicacion> ListaUbicaciones = t.getListaUbicaciones();

        verify(pDatosMock,times(1)).clearUbicaciones();
        verify(pDatosMock,times(1)).syncDB();
        verify(pDatosMock,times(1)).addUbicacion(coordenadas);
        verify(pDatosMock,times(1)).getListaUbicaciones();

    }

    @Test
    public void  CreacionUbicacion_escInvalido(){

        IntGestorMain pDatosMock = mock(IntGestorMain.class);

        GestorMain t = new GestorMain();

        t.inject(gestorGeocoding);
        t.inject(gestorUbicacion);
        t.inject(gestorSQLite);
        t.inject(gestorServicios);

        boolean resultado = t.clearUbicaciones();
        t.syncDB();
        String[] coordenadas={t.formatearCoordenada("39.98920"), t.formatearCoordenada("-0.03620")};
        t.addUbicacion(coordenadas);
        Map<String, Ubicacion> ListaUbicaciones = t.getListaUbicaciones();

        verify(pDatosMock,times(1)).clearUbicaciones();
        verify(pDatosMock,times(1)).syncDB();
        verify(pDatosMock,times(1)).addUbicacion(coordenadas);
        verify(pDatosMock,times(1)).getListaUbicaciones();

    }


}
