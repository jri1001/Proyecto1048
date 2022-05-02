package dao;

import modelo.Ubicacion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UbicacionRowMapper implements RowMapper<Ubicacion> {
    @Override
    public Ubicacion mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ubicacion ubic = new Ubicacion();
        ubic.setNombre(rs.getString("nombre"));
        ubic.setCiudad(rs.getString("ciudad"));
        ubic.setProvincia(rs.getString("provincia"));
        ubic.setCod_postal(rs.getString("cod_postal"));
        ubic.setLongitud(rs.getString("longitud"));
        ubic.setLatitud(rs.getString("latitud"));
        return ubic;
    }
}

