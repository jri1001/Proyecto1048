package mock_Ubicacion;

import modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class PI_Ubicacion {
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
    public void TestActivarUbicacion(){
         //Given
        String nombreUbic="Ubicacion";
        Ubicacion ubicacion=new Ubicacion();
        ubicacion.setNombre(gestorMain.formatearToponimo(nombreUbic));
        gestorUbicacion.addUbicacion(ubicacion);

        //When
        //La primera vez se añade activa la ubicacion (true), la segunda se queja (false)
        Mockito.when(mockedGestorSQLite.activarUbicacion(Mockito.anyString())).thenReturn(true).thenReturn(false);
        boolean res1=gestorMain.activarUbicacion(nombreUbic);
        boolean res2=gestorMain.activarUbicacion(nombreUbic);
        //Then
        assertTrue(res1);
        assertFalse(res2);
        assertEquals(gestorMain.getListaUbicacionesActivas().get(0),gestorMain.formatearToponimo(nombreUbic));
    }

    @Test
    public void TestDesactivarUbicacion(){
        //Given
        String nombreUbic="Ubicacion";
        Ubicacion ubicacion=new Ubicacion();
        ubicacion.setNombre(gestorMain.formatearToponimo(nombreUbic));
        gestorUbicacion.addUbicacion(ubicacion);
        Mockito.when(mockedGestorSQLite.activarUbicacion(Mockito.anyString())).thenReturn(true);
        gestorMain.activarUbicacion(nombreUbic);

        //When
        //La primera vez se desactiva una ubicacion (true), la segunda se queja (false)
        Mockito.when(mockedGestorSQLite.desactivarUbicacion(Mockito.anyString())).thenReturn(true).thenReturn(false);
        boolean res1=gestorMain.desactivarUbicacion(nombreUbic);
        boolean res2=gestorMain.desactivarUbicacion(nombreUbic);
        //Then
        assertTrue(res1);
        assertFalse(res2);
        assertTrue(gestorMain.getListaUbicacionesActivas().isEmpty());
    }

    @Test
    public void TestClearUbicaciones(){
        //Given
        String nombreUbic="Ubicacion";
        Ubicacion ubicacion=new Ubicacion();
        ubicacion.setNombre(gestorMain.formatearToponimo(nombreUbic));
        gestorUbicacion.addUbicacion(ubicacion);
        //When
        int size1=gestorMain.getListaUbicaciones().size();
        Mockito.when(mockedGestorSQLite.clearUbicaciones()).thenReturn(true);
        boolean res=gestorMain.clearUbicaciones();
        int size2=gestorMain.getListaUbicaciones().size();
        //Then
        assertTrue(res);
        assertEquals(size1,1);
        assertEquals(size2,0);
    }

    @Test
    public void TestAddUbicacion(){
         //Given
        String nombreUbic="Ubicacion";
        Ubicacion ubicacion=new Ubicacion();
        ubicacion.setNombre(nombreUbic);
        ubicacion.setCod_postal("46012");
        ubicacion.setLatitud("1.1234567");
        ubicacion.setLongitud("1.1234567");
        GestorGeocoding mockedGestorGeocoding=Mockito.mock(GestorGeocoding.class);
        gestorMain.inject(mockedGestorGeocoding);
        //When
        //En el primer caso es una ubicacion dentro de españa, en el segundo caso no (null)
        Mockito.when(mockedGestorGeocoding.peticion(Mockito.anyString())).thenReturn(ubicacion).thenReturn(null);
        Mockito.when(mockedGestorSQLite.addUbicacion(Mockito.any(Ubicacion.class))).thenReturn(true);
        Ubicacion res1=gestorMain.addUbicacion(nombreUbic);
        Ubicacion res2=gestorMain.addUbicacion("nombreInvalido");
        //Then
        assertEquals(ubicacion,res1);
        assertNull(res2);
        assertEquals(gestorMain.getListaUbicaciones().get(nombreUbic),ubicacion);
    }

    @Test
    public void TestDeleteUbicacion(){
        //Given
        String nombreUbic="Ubicacion";
        Ubicacion ubicacion=new Ubicacion();
        ubicacion.setNombre(gestorMain.formatearToponimo(nombreUbic));
        gestorUbicacion.addUbicacion(ubicacion);
        //When
        //Primero elimina la ubicacion existente, luego se queja
        Mockito.when(mockedGestorSQLite.deleteUbicacion(Mockito.anyString())).thenReturn(true).thenReturn(false);
        boolean res1=gestorMain.deleteUbicacion(nombreUbic);
        boolean res2=gestorMain.deleteUbicacion(nombreUbic);
        //Then
        assertTrue(res1);
        assertFalse(res2);
        assertTrue(gestorMain.getListaUbicaciones().isEmpty());
    }

    @Test
    public void TestGetUbicacion(){
         //Given
        Ubicacion ubicacion=new Ubicacion();
        ubicacion.setNombre("ubicacion0");
        gestorUbicacion.addUbicacion(ubicacion);
        gestorUbicacion.activarUbicacion("ubicacion0");
        ubicacion.setNombre("ubicacion12");
        gestorUbicacion.addUbicacion(ubicacion);
        gestorUbicacion.activarUbicacion("ubicacion12");
        for(int i=1;i<10;i++){
            gestorUbicacion.addUbicacionReciente("ubicacion"+i);
        }
        //When
        Mockito.when(mockedGestorSQLite.addUbicacionReciente(Mockito.anyString())).thenReturn(true);
        Mockito.when(mockedGestorSQLite.deleteUbicacionRecienteMasAntigua()).thenReturn("ubicacion1");
        int res1=gestorUbicacion.getListaUbicacionesRecientes().size();
        gestorMain.getUbicacion("ubicacion0");
        int res2=gestorUbicacion.getListaUbicacionesRecientes().size();
        gestorMain.getUbicacion("ubicacion0");
        int res3=gestorUbicacion.getListaUbicacionesRecientes().size();
        gestorMain.getUbicacion("ubicacion12");
        int res4=gestorUbicacion.getListaUbicacionesRecientes().size();
        //Then
        assertEquals(9, res1);
        assertEquals(10, res2);
        assertEquals(res2,res3,res4);
        assertFalse(gestorUbicacion.getListaUbicacionesRecientes().contains("ubicacion1"));
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
        boolean res1=gestorMain.deleteAlias(nombreUbic, nombreAlias);
        boolean res2=gestorMain.deleteAlias(nombreUbic, nombreAlias);
        //Then
        assertTrue(res1);
        assertFalse(res2);
        assertTrue(gestorMain.getListaAlias(nombreUbic).isEmpty());
    }

}
