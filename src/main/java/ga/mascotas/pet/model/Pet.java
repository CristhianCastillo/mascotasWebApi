package ga.mascotas.pet.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Pets")
public class Pet {

	@Id
	private String id;
	private String idUsuario;
	private String imagen;
	private String nombre;
	private String idTipo;
	private String genero;
	private Date fechaNacimiento;
	private String raza;
	private boolean esterilizado;
	private String color;
	private String descripcion;
	
	public Pet() {	
	}
		
	public Pet(String id, String idUsuario, String imagen, String nombre, String idTipo, String genero,
			Date fechaNacimiento, String raza, boolean esterilizado, String color, String descripcion) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.imagen = imagen;
		this.nombre = nombre;
		this.idTipo = idTipo;
		this.genero = genero;
		this.fechaNacimiento = fechaNacimiento;
		this.raza = raza;
		this.esterilizado = esterilizado;
		this.color = color;
		this.descripcion = descripcion;
	}

	public Pet(String idUsuario, String imagen, String nombre, String idTipo, String genero, Date fechaNacimiento,
			String raza, boolean esterilizado, String color, String descripcion) {
		this.idUsuario = idUsuario;
		this.imagen = imagen;
		this.nombre = nombre;
		this.idTipo = idTipo;
		this.genero = genero;
		this.fechaNacimiento = fechaNacimiento;
		this.raza = raza;
		this.esterilizado = esterilizado;
		this.color = color;
		this.descripcion = descripcion;
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
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIdTipo() {
		return idTipo;
	}
	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getRaza() {
		return raza;
	}
	public void setRaza(String raza) {
		this.raza = raza;
	}
	public boolean isEsterilizado() {
		return esterilizado;
	}
	public void setEsterilizado(boolean esterilizado) {
		this.esterilizado = esterilizado;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
