package ga.mascotas.request.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ga.mascotas.establishment.model.Establishment;
import ga.mascotas.pet.model.Pet;
import ga.mascotas.request.util.RequestConstants;
import ga.mascotas.request.model.Request;
import ga.mascotas.request.repository.RequestRepository;
import ga.mascotas.serviceType.model.ServiceType;
import ga.mascotas.user.model.User;
import ga.mascotas.util.ResponseDto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@CrossOrigin
public class RequestController {
	@Autowired
	private RequestRepository repository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
			
	@GetMapping(RequestConstants.RETRIVE_REST)
	public ResponseDto<List<Request>> getRequest() {
		try {
			return ResponseDto.success(this.repository.findAll());
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@GetMapping(RequestConstants.RETRIVE_OWNER_TOP_REST + "/{idUsuario}/{limit}")
	public ResponseDto<String> getRequestsOwnerTop(@PathVariable String idUsuario, @PathVariable int limit) {
		try {
			Establishment establecimiento =  this.mongoTemplate.findOne(Query.query(Criteria.where("idUsuario").is(idUsuario)), Establishment.class);
			Query query = new Query();
			query.limit(limit);
			query.with(new Sort(Sort.Direction.DESC, "fecha"));
			query.addCriteria(Criteria.where("idEstablecimiento").is(establecimiento.getId()));
			query.addCriteria(Criteria.where("mostrarUsuario").is(true));
			List<Request> listRequest = this.mongoTemplate.find(query, Request.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode listaSolicitudes = mapper.createArrayNode();
			for(int i = 0; i < listRequest.size(); i ++) {
				Request solicitudTemp = listRequest.get(i);
				ObjectNode solicitud = mapper.createObjectNode();
				solicitud.put("id", solicitudTemp.getId());
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				solicitud.put("fecha", dateFormat.format(solicitudTemp.getFecha()));
				solicitud.put("estado", solicitudTemp.isEstado());
				ServiceType actividad = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(solicitudTemp.getIdTipo())), ServiceType.class);
				solicitud.put("tipoActividad", actividad.getNombre());
				solicitud.put("mensaje", solicitudTemp.getMensaje());
				solicitud.put("respuesta", solicitudTemp.getRespuesta());
				Pet pet = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(solicitudTemp.getIdMascota())), Pet.class);
				if(pet == null)
					solicitud.put("mascota", "No Identificada");
				else
					solicitud.put("mascota", pet.getNombre());
				User user = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(pet.getIdUsuario())), User.class);
				solicitud.put("usuario", user.getUsername());
				((ArrayNode) listaSolicitudes).add(solicitud);
			}
			return ResponseDto.success(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listaSolicitudes));
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@GetMapping(RequestConstants.RETRIVE_USERS + "/{idUsuario}")
	public ResponseDto<String> getRequestsUser(@PathVariable String idUsuario) {
		try {
			User usuario =  this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(idUsuario)), User.class);
			List<Pet> listaMascotas = this.mongoTemplate.find(Query.query(Criteria.where("idUsuario").is(idUsuario)), Pet.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode listaSolicitudes = mapper.createArrayNode();
			for(int g = 0; g < listaMascotas.size(); g ++) {
				Pet pet = listaMascotas.get(g);
				Query query = new Query();
				query.with(new Sort(Sort.Direction.DESC, "fecha"));
				query.addCriteria(Criteria.where("idMascota").is(listaMascotas.get(g).getId()));
				query.addCriteria(Criteria.where("mostrarCliente").is(true));
				List<Request> listRequest = this.mongoTemplate.find(query, Request.class);
				for(int i = 0; i < listRequest.size(); i ++) {
					Request solicitudTemp = listRequest.get(i);
					ObjectNode solicitud = mapper.createObjectNode();
					solicitud.put("id", solicitudTemp.getId());
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					solicitud.put("fecha", dateFormat.format(solicitudTemp.getFecha()));
					Establishment establecimiento = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(solicitudTemp.getIdEstablecimiento())), Establishment.class);
					solicitud.put("estado", solicitudTemp.isEstado());
					solicitud.put("establecimiento", establecimiento.getNombre());
					ServiceType actividad = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(solicitudTemp.getIdTipo())), ServiceType.class);
					solicitud.put("tipoActividad", actividad.getNombre());
					solicitud.put("mensaje", solicitudTemp.getMensaje());
					solicitud.put("respuesta", solicitudTemp.getRespuesta());
					//Pet pet = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(solicitudTemp.getIdMascota())), Pet.class);
					solicitud.put("mascota", pet.getNombre());
					//User user = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(pet.getIdUsuario())), User.class);
					solicitud.put("usuario", usuario.getUsername());
					((ArrayNode) listaSolicitudes).add(solicitud);
				}
			}
			return ResponseDto.success(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listaSolicitudes));
		} catch (Exception ex){
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@PostMapping(RequestConstants.RETRIVE_OWNER_DATE + "/{idUsuario}")
	public ResponseDto<String> getRequestsOwnerDate(@PathVariable String idUsuario, @RequestBody Map<String , String> body) throws Exception {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
			String fechaInicial = body.get("fechaInicial") + "T00:00:00.000Z";
			String fechaFinal = body.get("fechaFinal") + "T00:00:00.000Z";
			Date dateInicial = format.parse(fechaInicial);
			System.out.println(dateInicial);
			Date dateFinal = format.parse(fechaFinal);
			System.out.println(dateFinal);
			Establishment establecimiento =  this.mongoTemplate.findOne(Query.query(Criteria.where("idUsuario").is(idUsuario)), Establishment.class);
			Query query = new Query();
			query.with(new Sort(Sort.Direction.DESC, "fecha"));
			query.addCriteria(Criteria.where("idEstablecimiento").is(establecimiento.getId()));
			query.addCriteria(Criteria.where("fecha").lt(format.parse(fechaFinal)).gt(format.parse(fechaInicial)));
			query.addCriteria(Criteria.where("mostrarUsuario").is(true));
			List<Request> listRequest = this.mongoTemplate.find(query, Request.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode listaSolicitudes = mapper.createArrayNode();
			for(int i = 0; i < listRequest.size(); i ++) {
				Request solicitudTemp = listRequest.get(i);
				ObjectNode solicitud = mapper.createObjectNode();
				solicitud.put("id", solicitudTemp.getId());
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				solicitud.put("fecha", dateFormat.format(solicitudTemp.getFecha()));
				solicitud.put("estado", solicitudTemp.isEstado());
				ServiceType actividad = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(solicitudTemp.getIdTipo())), ServiceType.class);
				solicitud.put("tipoActividad", actividad.getNombre());
				solicitud.put("mensaje", solicitudTemp.getMensaje());
				solicitud.put("respuesta", solicitudTemp.getRespuesta());
				Pet pet = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(solicitudTemp.getIdMascota())), Pet.class);
				if(pet == null)
					solicitud.put("mascota","No Identificada");
				else
					solicitud.put("mascota", pet.getNombre());
				User user = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(pet.getIdUsuario())), User.class);
				solicitud.put("usuario", user.getUsername());
				((ArrayNode) listaSolicitudes).add(solicitud);
			}
			return ResponseDto.success(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listaSolicitudes));
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@PostMapping(RequestConstants.CREATE_REST)
	public ResponseDto<Request> addRequest(@RequestBody Map<String, String> body) {
		try {
			String idMascota = body.get("idMascota");
			String idTipo = body.get("idTipo");
			String idEstablecimiento = body.get("idEstablecimiento");
			String mensaje = body.get("mensaje");

			Request solicitud = new Request();
			solicitud.setId(ObjectId.get().toHexString());
			solicitud.setIdMascota(idMascota);
			solicitud.setIdTipo(idTipo);
			solicitud.setIdEstablecimiento(idEstablecimiento);
			Date date = new Date();
			solicitud.setFecha(date);
			solicitud.setMensaje(mensaje);
			solicitud.setEstado(false);
			solicitud.setRespuesta("");
			solicitud.setMostrarUsuario(true);
			solicitud.setMostrarCliente(true);
			Request result = this.repository.insert(solicitud);
			return ResponseDto.success(result);
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@PutMapping(RequestConstants.SEND_REST + "/{id}")
	public ResponseDto<Request> sendResponse(@PathVariable String id, @RequestBody Map<String, String> body) throws Exception {
		try {
			String respuesta = body.get("respuesta");
			Request solicitud = this.repository.findByid(id);
			solicitud.setRespuesta(respuesta);
			solicitud.setEstado(true);
			Request result = this.repository.save(solicitud);
			return ResponseDto.success(result);
		} catch (Exception ex){
			return ResponseDto.error(ex.getMessage());
		}
	}

	@DeleteMapping(RequestConstants.DELETE_OWNER_REST + "/{id}")
	public ResponseDto<Request> deleteRequestOwner(@PathVariable String id) {
		try {
			Request request = this.repository.findByid(id);
			request.setMostrarUsuario(false);
			request = this.repository.save(request);
			return ResponseDto.success(request);
		}
		catch(Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@DeleteMapping(RequestConstants.DELETE_USER_REST + "/{id}")
	public ResponseDto<Request> deleteRequestUser(@PathVariable String id) {
		try {
			Request request = this.repository.findByid(id);
			request.setMostrarCliente(false);
			request = this.repository.save(request);
			return ResponseDto.success(request);
		}
		catch(Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
}