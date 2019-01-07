package ga.mascotas.establishment.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Establishments")
public class Establishment {

    @Id
    private String id;
    private String idUsuario;
    private ArrayList<String> servicios;
    @Indexed(unique = true)
    private String nit;
    @Indexed(unique = true)
    private String nombre;
    private String telefono;
    private String direccion;
    private String email;
    private String paginaWeb;
    private String horarios;
    private String horaInicial;
    private String horaFinal;
    private String descripcion;
    private String imagen;
    private int calificacionesBuenas;
    private int calificacionesMalas;
    private boolean estado;

    public Establishment() {

    }

    //Creación de un establecimiento.
    public Establishment(String id, String idUsuario, String nit, String nombre,
                         String descripcion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.servicios = new ArrayList<>();
        this.nit = nit;
        this.nombre = nombre;
        this.telefono = "";
        this.direccion = "";
        this.email = "";
        this.paginaWeb = "";
        this.horarios = "";
        this.horaInicial = "";
        this.horaFinal = "";
        this.descripcion = descripcion;
        this.imagen = "";
        this.calificacionesBuenas = 0;
        this.calificacionesMalas = 0;
        this.estado = false;
    }

    public int getCalificacionesBuenas() {
        return calificacionesBuenas;
    }

    public void setCalificacionesBuenas(int calificacionesBuenas) {
        this.calificacionesBuenas += calificacionesBuenas;
    }

    public int getCalificacionesMalas() {
        return calificacionesMalas;
    }

    public void setCalificacionesMalas(int calificacionesMalas) {
        this.calificacionesMalas += calificacionesMalas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public ArrayList<String> getServicios() {
        return servicios;
    }

    public void setServicios(ArrayList<String> servicios) {
        this.servicios = servicios;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
