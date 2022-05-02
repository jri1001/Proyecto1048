package modelo;

public class Ubicacion {
    private String nombre;
    private String ciudad;
    private String provincia;
    private String cod_postal;
    private String longitud;
    private String latitud;

    public Ubicacion(){}

    public Ubicacion(String nombre, String ciudad, String provincia, String cod_postal, String longitud, String latitud) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.cod_postal = cod_postal;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre.replaceAll("\"","");
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad.replaceAll("\"","");
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia.replaceAll("\"","");
    }

    public String getCod_postal() {
        return cod_postal;
    }

    public void setCod_postal(String cod_postal) {
        this.cod_postal = cod_postal.replaceAll("\"","");
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud.replaceAll("\"","");
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud.replaceAll("\"","");
    }


    @Override
    public String toString() {
        return "Ubicacion{" +
                "nombre='" + nombre + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", provincia='" + provincia + '\'' +
                ", cod_postal='" + cod_postal + '\'' +
                ", longitud='" + longitud + '\'' +
                ", latitud='" + latitud + '\'' +
                '}';
    }
}
