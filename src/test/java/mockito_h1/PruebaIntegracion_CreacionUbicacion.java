package mockito_h1;

import modelo.*;
import org.junit.Test;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class PruebaIntegracion_CreacionUbicacion {
    GestorGeocoding gestorGeocoding = new GestorGeocoding();
    GestorUbicacion gestorUbicacion = new GestorUbicacion();
    GestorSQLite gestorSQLite = new GestorSQLite();

    @Test
    public void  CreacionUbicacion_escValido(){

        IntGestorMain pDatosMock = mock(IntGestorMain.class);

        GestorMain t = new GestorMain();

        t.inject(gestorGeocoding);
        t.inject(gestorUbicacion);
        t.inject(gestorSQLite);
        boolean resultado = t.clearUbicaciones();
        Map<String, Ubicacion> ListaUbicaciones = t.getListaUbicaciones();

        verify(pDatosMock,times(1)).clearUbicaciones();
        verify(pDatosMock,times(1)).syncDB();
        verify(pDatosMock,times(1)).addUbicacion("Valencia");
        verify(pDatosMock,times(1)).addUbicacion("Benimaclet");
        verify(pDatosMock,times(3)).getListaUbicaciones();


    }

    @Test
    public void  CreacionUbicacion_escInvalido(){

        IntGestorMain pDatosMock = mock(IntGestorMain.class);

        GestorMain t = new GestorMain();

        t.inject(gestorGeocoding);
        t.inject(gestorUbicacion);
        t.inject(gestorSQLite);
        boolean resultado = t.clearUbicaciones();
        Map<String, Ubicacion> ListaUbicaciones = t.getListaUbicaciones();

        verify(pDatosMock,times(1)).clearUbicaciones();
        verify(pDatosMock,times(1)).syncDB();
        verify(pDatosMock,times(1)).addUbicacion("Argentina");
        verify(pDatosMock,times(1)).getListaUbicaciones();

    }


}
