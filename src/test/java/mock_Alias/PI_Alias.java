package mock_Alias;

import modelo.GestorMain;
import modelo.GestorSQLite;
import modelo.GestorUbicacion;
import modelo.Ubicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class PI_Alias {
    private GestorSQLite mockedGestorSQLite;
    private GestorMain gestorMain;
    private GestorUbicacion gestorUbicacion;

     @BeforeEach
    void setUp(){
         gestorMain=new GestorMain();
         mockedGestorSQLite= Mockito.mock(GestorSQLite.class);
         gestorMain.inject(mockedGestorSQLite);
         gestorUbicacion=new GestorUbicacion();
         gestorMain.inject(gestorUbicacion);
     }

    @Test
    public void TestAddAlias(){
         //Given
        String nombreUbic="Ubicacion";
        String nombreAlias="Alias1";
        Ubicacion ubicacion=new Ubicacion();
        ubicacion.setNombre(nombreUbic);
        gestorUbicacion.addUbicacion(ubicacion);

        //When
        //La primera vez se añade un alias no existente (true), la segunda se queja (false)
        Mockito.when(mockedGestorSQLite.addAlias(Mockito.anyString(),Mockito.anyString())).thenReturn(true).thenReturn(false);
        boolean res1=gestorMain.addAlias(nombreUbic, nombreAlias);
        boolean res2=gestorMain.addAlias(nombreUbic, nombreAlias);
        //Then
        assertTrue(res1);
        assertFalse(res2);
        assertEquals(gestorMain.getListaAlias(nombreUbic).get(0),nombreAlias);
    }

    @Test
    public void TestDeleteAlias(){
        //Given
        String nombreUbic="Ubicacion";
        String nombreAlias="Alias1";
        Ubicacion ubicacion=new Ubicacion();
        ubicacion.setNombre(nombreUbic);
        gestorUbicacion.addUbicacion(ubicacion);
        gestorUbicacion.addAlias(gestorMain.formatearToponimo(nombreUbic),nombreAlias);

        //When
        //La primera vez se elimina un alias existente (true), la segunda se queja (false)
        Mockito.when(mockedGestorSQLite.deleteAlias(Mockito.anyString(),Mockito.anyString())).thenReturn(true).thenReturn(false);
        Boolean res1=gestorMain.deleteAlias(nombreUbic, nombreAlias);
        Boolean res2=gestorMain.deleteAlias(nombreUbic, nombreAlias);
        //Then
        assertTrue(res1);
        assertFalse(res2);
        assertTrue(gestorMain.getListaAlias(nombreUbic).isEmpty());
    }


}
