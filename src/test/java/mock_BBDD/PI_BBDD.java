package mock_BBDD;

import modelo.GestorMain;
import modelo.GestorSQLite;
import modelo.GestorUbicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PI_BBDD {
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
    public void TestSyncDB(){

    }

    @Test
    public void TestCambiarDB(){}
}
