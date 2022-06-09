package mock_BBDD;

import modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PI_BBDD {
    private GestorSQLite mockedGestorSQLite;
    private GestorMain gestorMain;

    @BeforeEach
    void setUp(){
         gestorMain=new GestorMain();
         mockedGestorSQLite= Mockito.mock(GestorSQLite.class);
         gestorMain.inject(mockedGestorSQLite);
    }

    @Test
    public void TestSyncDB(){
        //Given
        GestorUbicacion gestorUbicacion=new GestorUbicacion();
        gestorMain.inject(gestorUbicacion);
        GestorServicios gestorServicios=new GestorServicios();
        gestorMain.inject(gestorServicios);
        Ubicacion ubicacion=new Ubicacion();
        String ubicNombre="ubicacion";
        String servicio="TicketMaster";
        ubicacion.setNombre(gestorMain.formatearToponimo(ubicNombre));
        List<Ubicacion> listaUbic=new ArrayList<Ubicacion>(); listaUbic.add(ubicacion);
        HashSet<String> listaUbicActiva=new HashSet<String>(); listaUbicActiva.add(ubicNombre);
        HashSet<String> listaServActivos=new HashSet<String>(); listaServActivos.add(servicio);
        //When
        Mockito.when(mockedGestorSQLite.getListaUbicaciones()).thenReturn(listaUbic);
        Mockito.when(mockedGestorSQLite.getListaUbicacionesActivas()).thenReturn(listaUbicActiva);
        Mockito.when(mockedGestorSQLite.getListaUbicacionesRecientes()).thenReturn(new HashSet<>());
        Mockito.when(mockedGestorSQLite.getListaAlias()).thenReturn(new HashMap<>());
        Mockito.when(mockedGestorSQLite.getListaServiciosActivos()).thenReturn(listaServActivos);
        Mockito.when(mockedGestorSQLite.getListaServiciosUbicacion()).thenReturn(new HashMap<>());
        Map<String, Ubicacion> mapubic1=gestorUbicacion.getListaUbicacion();
        List<String> listaserv1=gestorServicios.getListaServiciosActivos();
        gestorMain.syncDB();
        Map<String, Ubicacion> mapubic2=gestorUbicacion.getListaUbicacion();
        List<String> listaserv2=gestorServicios.getListaServiciosActivos();
        //Then
        assertTrue(listaserv1.isEmpty());
        assertTrue(mapubic1.isEmpty());
        assertEquals(listaserv2.get(0),servicio);
        assertEquals(mapubic2.get(ubicNombre),ubicacion);
    }

    @Test
    public void TestCambiarDB(){
        //Given
        String nombre1="BBDD_Existe.db";
        String nombre2="BBDD_NoExistente.asdasd";
        //When
        Mockito.when(mockedGestorSQLite.cambiarDB(Mockito.anyString())).thenReturn(true);
        boolean res1=gestorMain.cambiarDB(nombre1);
        boolean res2=gestorMain.cambiarDB(nombre2);
        //Then
        Mockito.verify(mockedGestorSQLite,Mockito.times(1)).cambiarDB(Mockito.anyString());
        assertTrue(res1);
        assertFalse(res2);
    }
}
