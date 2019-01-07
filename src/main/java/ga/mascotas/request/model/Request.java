package ga.mascotas.request.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Requests")
public class Request {
	@Id
	private String id;
	private String idMascota;
	private String idTipo;
	private String idEstablecimiento;
	private Date fecha;
	private String mensaje;
	private String respuesta;
	private boolean estado;
	private boolean mostrarUsuario;
	private boolean mostrarCliente;
	
	public Request() {
		
	}

	public Request(String id, String idMascota, String idTipo, String idEstablecimiento, Date fecha, String mensaje,
			String respuesta, boolean estado, boolean mostrarUsuario, boolean mostrarCliente) {
		this.id = id;
		this.idMascota = idMascota;
		this.idTipo = idTipo;
		this.idEstablecimiento = idEstablecimiento;
		this.fecha = fecha;
		this.mensaje = mensaje;
		this.respuesta = respuesta;
		this.estado = estado;
		this.mostrarUsuario = mostrarUsuario;
		this.mostrarCliente = mostrarCliente;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}



	public boolean isMostrarUsuario() {
		return mostrarUsuario;
	}



	public void setMostrarUsuario(boolean mostrarUsuario) {
		this.mostrarUsuario = mostrarUsuario;
	}



	public boolean isMostrarCliente() {
		return mostrarCliente;
	}



	public void setMostrarCliente(boolean mostrarCliente) {
		this.mostrarCliente = mostrarCliente;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdMascota() {
		return idMascota;
	}

	public void setIdMascota(String idMascota) {
		this.idMascota = idMascota;
	}

	public String getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}

	public String getIdEstablecimiento() {
		return idEstablecimiento;
	}

	public void setIdEstablecimiento(String idEstablecimiento) {
		this.idEstablecimiento = idEstablecimiento;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
