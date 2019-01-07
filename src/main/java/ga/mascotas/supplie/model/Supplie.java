package ga.mascotas.supplie.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Supplies")
public class Supplie {
	@Id
	private String id;
	private String idUsuario;
	private String idTipo;
	private String nombre;
	private double cantidad;
	private String unidadMedida;
	private Date fecha;
	private double precio;
	private String proveedor;
	private String comentario;
	
	public Supplie() {
		
	}
	
	

	public Supplie(String id, String idUsuario, String idTipo, String nombre, double cantidad, String unidadMedida,
			Date fecha, double precio, String proveedor, String comentario) {
		this.id = id;
		this.idUsuario = idUsuario;
		this.idTipo = idTipo;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.unidadMedida = unidadMedida;
		this.fecha = fecha;
		this.precio = precio;
		this.proveedor = proveedor;
		this.comentario = comentario;
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

	public String getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
}
