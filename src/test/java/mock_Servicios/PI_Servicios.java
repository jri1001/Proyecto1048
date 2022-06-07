package mock_Servicios;

import modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class PI_Servicios {
    private GestorSQLite mockedGestorSQLite;
    private GestorServicios gestorServicios;
    private GestorTicketMaster mockedGestorTicketMaster;
    private GestorNewsDataIO mockedGestorNewsDataIO;
    private  GestorOpenWeather MockedGestorOpenWeather;
    private GestorMain gestorMain;
     @BeforeEach
    void setUp(){
         gestorServicios=new GestorServicios();
         mockedGestorTicketMaster=Mockito.mock(GestorTicketMaster.class);
         gestorServicios.inject(mockedGestorTicketMaster);
         mockedGestorNewsDataIO=Mockito.mock(GestorNewsDataIO.class);
         gestorServicios.inject(mockedGestorNewsDataIO);
         MockedGestorOpenWeather=Mockito.mock(GestorOpenWeather.class);
         gestorServicios.inject(MockedGestorOpenWeather);

         gestorMain=new GestorMain();
         mockedGestorSQLite= Mockito.mock(GestorSQLite.class);
         gestorMain.inject(mockedGestorSQLite);
         gestorMain.inject(gestorServicios);
     }

}
