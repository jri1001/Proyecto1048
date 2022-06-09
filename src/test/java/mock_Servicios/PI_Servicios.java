package mock_Servicios;

import modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PI_Servicios {
    private GestorSQLite mockedGestorSQLite;
    private GestorGeocoding mockedGestorGeocoding;
    private GestorServicios gestorServicios;
    private GestorTicketMaster mockedGestorTicketMaster;
    private GestorNewsDataIO mockedGestorNewsDataIO;
    private GestorOpenWeather mockedGestorOpenWeather;
    private GestorUbicacion gestorUbicacion;
    private GestorMain gestorMain;

    @BeforeEach
    void setUp(){
         gestorServicios=new GestorServicios();
         mockedGestorTicketMaster=Mockito.mock(GestorTicketMaster.class);
         gestorServicios.inject(mockedGestorTicketMaster);
         mockedGestorNewsDataIO=Mockito.mock(GestorNewsDataIO.class);
         gestorServicios.inject(mockedGestorNewsDataIO);
         mockedGestorOpenWeather=Mockito.mock(GestorOpenWeather.class);
         gestorServicios.inject(mockedGestorOpenWeather);
         gestorUbicacion=new GestorUbicacion();
         gestorMain=new GestorMain();
         mockedGestorSQLite= Mockito.mock(GestorSQLite.class);
         mockedGestorGeocoding= Mockito.mock(GestorGeocoding.class);

         gestorMain.inject(mockedGestorSQLite);
         gestorMain.inject(gestorServicios);
         gestorMain.inject(mockedGestorGeocoding);
         gestorMain.inject(gestorUbicacion);
    }

    @Test
    public void TestActivarServicio(){
         //Given
         //When
         Mockito.when(mockedGestorSQLite.addServicioActivo(Mockito.anyString())).thenReturn(true);
         boolean res1=gestorMain.activarServicio("TicketMaster");
         boolean res2=gestorMain.activarServicio("TicketMaster");
         boolean res3=gestorMain.activarServicio("ServicioInexistente");
         //Then
         assertTrue(res1);
         assertFalse(res2);
         assertFalse(res3);
    }

    @Test
    public void TestDesactivarServicio(){
        //Given
        Mockito.when(mockedGestorSQLite.addServicioActivo(Mockito.anyString())).thenReturn(true);
        Mockito.when(mockedGestorSQLite.deleteServicioActivo(Mockito.anyString())).thenReturn(true);
        gestorMain.activarServicio("TicketMaster");
        //When

        boolean res1=gestorMain.desactivarServicio("TicketMaster");
        boolean res2=gestorMain.desactivarServicio("TicketMaster");
        boolean res3=gestorMain.desactivarServicio("ServicioInexistente");
        //Then
        assertTrue(res1);
        assertFalse(res2);
        assertFalse(res3);
    }

    @Test
    public void TestinfoUbicacionServicios(){
         //Given
         String nombreUbic="ubicacion";
         Ubicacion ubicacion=new Ubicacion();
         ubicacion.setNombre(gestorMain.formatearToponimo(nombreUbic));
         Mockito.when(mockedGestorSQLite.addServicioActivo(Mockito.anyString())).thenReturn(true);
         gestorServicios.activarServicio("TicketMaster");
         gestorServicios.activarServicio("OpenWeather");
          ArrayList<HashMap<String,String>> respuesta=new ArrayList<>();
         //When
         //Todos los servicios activos devuelven un Array con la información relacionada con la ubicacion
         Mockito.when(mockedGestorGeocoding.peticion(Mockito.anyString())).thenReturn(ubicacion);
         Mockito.when(mockedGestorTicketMaster.peticion(Mockito.anyString())).thenReturn(respuesta);
         Mockito.when(mockedGestorNewsDataIO.peticion(Mockito.anyString())).thenReturn(respuesta);
         HashMap<String, ArrayList<HashMap<String, String>>> res= gestorMain.infoUbicacionServicios(nombreUbic);
         //Then
         assertNotNull(res.get("TicketMaster"));
         assertNotNull(res.get("OpenWeather"));
         assertNull(res.get("NewsDataIO"));
    }

    @Test
    public void TestInfoUbicacionActivaServicios(){
         //Given
        String nombreUbic1="ubicacion";
        Ubicacion ubicacion=new Ubicacion();
        ubicacion.setNombre(gestorMain.formatearToponimo(nombreUbic1));
        gestorUbicacion.addUbicacion(ubicacion);
        gestorUbicacion.activarUbicacion(nombreUbic1);
        String nombreUbic2="ubicacionNoactiva";
        ubicacion=new Ubicacion();
        ubicacion.setNombre(gestorMain.formatearToponimo(nombreUbic2));
        gestorUbicacion.addUbicacion(ubicacion);
        Mockito.when(mockedGestorSQLite.addServicioActivo(Mockito.anyString())).thenReturn(true);
        gestorServicios.activarServicio("TicketMaster");
        gestorServicios.activarServicio("OpenWeather");
        ArrayList<HashMap<String,String>> respuesta=new ArrayList<>();
        //When
        //Todos los servicios activos devuelven un Array con la información relacionada con la ubicacion
        Mockito.when(mockedGestorGeocoding.peticion(Mockito.anyString())).thenReturn(ubicacion);
        Mockito.when(mockedGestorTicketMaster.peticion(Mockito.anyString())).thenReturn(respuesta);
        Mockito.when(mockedGestorNewsDataIO.peticion(Mockito.anyString())).thenReturn(respuesta);
        HashMap<String, ArrayList<HashMap<String, String>>> res1= gestorMain.infoUbicacionActivaServicios(nombreUbic1);
        HashMap<String, ArrayList<HashMap<String, String>>> res2= gestorMain.infoUbicacionActivaServicios(nombreUbic2);
        //Then
        assertNotNull(res1.get("TicketMaster"));
        assertNotNull(res1.get("OpenWeather"));
        assertNull(res1.get("NewsDataIO"));
        assertTrue(res2.isEmpty());
    }

    @Test
    public void TestClearServicios(){
         //Given
        Mockito.when(mockedGestorSQLite.addServicioActivo(Mockito.anyString())).thenReturn(true);
        gestorServicios.activarServicio("TicketMaster");
        //When
        Mockito.when(mockedGestorSQLite.clearServiciosActivos()).thenReturn(true);
        List<String> list1=gestorServicios.getListaServiciosActivos();
        boolean res=gestorMain.clearServicios();
        List<String> list2=gestorServicios.getListaServiciosActivos();
        //Then
        assertTrue(res);
        assertFalse(list1.isEmpty());
        assertTrue(list2.isEmpty());
    }

    @Test
    public void TestAddServicioUbicacion(){
         //Given
        String nombreUbic="ubicacion";
        //When
        Mockito.when(mockedGestorSQLite.addServicioUbicacion(Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        List<String> list1=gestorMain.getListaServiciosUbicacion(nombreUbic);
        boolean res=gestorMain.addServicioUbicacion("TicketMaster",nombreUbic);
        List<String> list2=gestorMain.getListaServiciosUbicacion(nombreUbic);
        //Then
        assertTrue(res);
        assertTrue(list1.isEmpty());
        assertFalse(list2.isEmpty());
    }

    @Test
    public void TestDeleteServicioUbicacion(){
        //Given
        String nombreUbic="ubicacion";
        Mockito.when(mockedGestorSQLite.addServicioUbicacion(Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        gestorMain.addServicioUbicacion("TicketMaster",nombreUbic);
        //When
        Mockito.when(mockedGestorSQLite.deleteServicioUbicacion(Mockito.anyString(),Mockito.anyString())).thenReturn(true);
        List<String> list1=gestorMain.getListaServiciosUbicacion(nombreUbic);
        boolean res=gestorMain.deleteServicioUbicacion("TicketMaster",nombreUbic);
        List<String> list2=gestorMain.getListaServiciosUbicacion(nombreUbic);
        //Then
        assertTrue(res);
        assertFalse(list1.isEmpty());
        assertTrue(list2.isEmpty());
    }
}
