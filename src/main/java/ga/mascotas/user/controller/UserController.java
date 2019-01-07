package ga.mascotas.user.controller;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ga.mascotas.establishment.model.Establishment;
import ga.mascotas.request.model.Request;
import ga.mascotas.security.service.TokenService;
import ga.mascotas.serviceType.model.ServiceType;
import ga.mascotas.user.model.User;
import ga.mascotas.user.repository.UserRepository;
import ga.mascotas.user.util.Authority;
import ga.mascotas.user.util.UserConstants;
import ga.mascotas.userType.model.UserType;
import ga.mascotas.util.ResponseDto;
import ga.mascotas.util.LoginResultDto;
import ga.mascotas.util.Util;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	private UserRepository repository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private TokenService tokenService;

	@Autowired
	public UserController(final TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@GetMapping(UserConstants.RETRIVE_REST)
	public ResponseDto<List<User>> getUsers() {
		try {
			return ResponseDto.success(this.repository.findAll());
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@GetMapping("/DashBoards/countByServices/{idUsuario}")
	public Exportar getCountByServices(@PathVariable String idUsuario) {
		Establishment establecimiento = this.mongoTemplate.findOne(Query.query(Criteria.where("idUsuario").is(idUsuario)), Establishment.class);
		GroupOperation groupByStateAndSumPop = group("idTipo").count().as("solicitudes");
		Criteria priceCriteria = Criteria.where("idEstablecimiento").is(establecimiento.getId());
		MatchOperation matchOperation = match(priceCriteria);
        ProjectionOperation projectToMatchModel = project("solicitudes").and("idTipo").previousOperation();
		
        Aggregation aggregation = newAggregation(matchOperation, groupByStateAndSumPop, projectToMatchModel);
		AggregationResults<CountServices> result = mongoTemplate.aggregate(aggregation, Request.class, CountServices.class);
		List<CountServices> listaServicios = result.getMappedResults();
		ArrayList<String> nombresServicios = new ArrayList<String>();
		ArrayList<Integer> cantidadSolicitudes = new ArrayList<Integer>();
		for(int i = 0; i < listaServicios.size(); i ++) {
			ServiceType servicio = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(listaServicios.get(i).idTipo)), ServiceType.class);
			String nombre = servicio.getNombre();
			nombresServicios.add(nombre);
			cantidadSolicitudes.add(listaServicios.get(i).solicitudes);
		}
		Exportar expo = new Exportar();
		expo.servicios = nombresServicios;
		expo.numeros = cantidadSolicitudes;
		return expo;
	}
	
	@GetMapping("/DashBoards/countRatings/{idUsuario}")
	public Exportar getCountRatings(@PathVariable String idUsuario) {
		Establishment establecimiento = this.mongoTemplate.findOne(Query.query(Criteria.where("idUsuario").is(idUsuario)), Establishment.class);
		ArrayList<String> calificaciones = new ArrayList<String>();
		ArrayList<Integer> conteoCalificaciones = new ArrayList<Integer>();
		calificaciones.add("Buenas");
		calificaciones.add("Malas");
		conteoCalificaciones.add(establecimiento.getCalificacionesBuenas());
		conteoCalificaciones.add(establecimiento.getCalificacionesMalas());
		Exportar expo = new Exportar();
		expo.servicios = calificaciones;
		expo.numeros = conteoCalificaciones;
		return expo;
	}

	@PostMapping(UserConstants.CREATE_USER_REST)
	public ResponseDto<User> addUser(@RequestBody User user) {
		try {
			String id = ObjectId.get().toHexString();
			UserType tipoUsuario = this.mongoTemplate.findOne(Query.query(Criteria.where("nombre").is("Usuario")),UserType.class);
			String idTipo = tipoUsuario.getId();
			String passHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

			user.setId(id);
			user.setIdTipo(idTipo);
			user.setPassword(passHash);
			user.setAccountNonExpired(false);
			user.setCredentialsNonExpired(false);
			user.setEnabled(true);
			List<Authority> authorities = new ArrayList<>();
			authorities.add(Authority.ROLE_USER);
			user.setAuthorities(authorities);

			User userName = this.repository.findByUsername(user.getUsername());
			User userEmail = this.repository.findByEmail(user.getEmail());

			if (userName == null && userEmail == null) {
				User result = this.repository.insert(user);
				return ResponseDto.success(result);
			} else if (userName != null) {
                return ResponseDto.error("UserName");
			} else {
                return ResponseDto.error("Email");
			}
		} catch(Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}

	@PostMapping(UserConstants.CREATE_OWNER_REST)
	public ResponseDto<User> addOwner(@RequestBody Map<String, String> body) throws IOException {
		try {
			String id = ObjectId.get().toHexString();
			UserType tipoUsuario = this.mongoTemplate.findOne(Query.query(Criteria.where("nombre").is("Propietario")),UserType.class);
			String idTipo = tipoUsuario.getId();
			String nombre = body.get("nombre");
			String apellidos = body.get("apellidos");
			String email = body.get("email");
			String nombreUsuario = body.get("username");
			String password = body.get("password");
			String nombreEstablecimiento = body.get("establecimiento");
			String nit = body.get("nit");
			String descripcion = body.get("descripcion");

			String passHash = BCrypt.hashpw(password, BCrypt.gensalt());
			User user = new User(id, idTipo, nombre, apellidos, email, nombreUsuario, passHash);
			user.setAccountNonExpired(false);
			user.setCredentialsNonExpired(false);
			user.setEnabled(true);
			List<Authority> authorities = new ArrayList<>();
			authorities.add(Authority.ROLE_OWNER);
			user.setAuthorities(authorities);

			Establishment establecimiento = new Establishment(ObjectId.get().toHexString(), user.getId(), nit,
					nombreEstablecimiento, descripcion);

			User userName = this.repository.findByUsername(nombreUsuario);
			User userEmail = this.repository.findByEmail(email);
			Establishment establecimientoNombre = this.mongoTemplate.findOne(Query.query(Criteria.where("nombre").is(nombreEstablecimiento)),
					Establishment.class);
			Establishment establecimientoNit = this.mongoTemplate.findOne(Query.query(Criteria.where("nit").is(nit)),
					Establishment.class);

			if (userName == null && userEmail == null && establecimientoNombre == null && establecimientoNit == null) {
				User result = this.repository.insert(user);
				this.mongoTemplate.insert(establecimiento, "Establishments");
				return ResponseDto.success(result);
			} else if (userName != null) {
				return ResponseDto.error("UserName");
			} else if (userEmail != null) {
				return ResponseDto.error("Email");
			} else if (establecimientoNombre != null) {
				return ResponseDto.error("Establishment");
			} else {
				return ResponseDto.error("Nit");
			}
		} catch(Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}

	/**
	 * Autentificaci&oacute;n de usuarios.
	 * @param body Credenciales del usuario. body != null.
	 * @return Resultado de la operaci&oacute;n.
	 */
	@PostMapping(UserConstants.LOGIN_REST)
	public ResponseDto<LoginResultDto> loginUsers(@RequestBody Map<String, String> body) {
		try {
			String usuario = body.get("usuario");
			String password = body.get("password");
			final LoginResultDto result = tokenService.getToken(usuario, password);
			if (result != null) {
				return ResponseDto.success(result);
			} else {
				return ResponseDto.error("Usuario o contraseña no valido(s).");
			}
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}

	@PutMapping(UserConstants.UPDATE_REST + "/{id}")
	public ResponseDto<User> updateUser(@PathVariable String id, @RequestBody User user) {
		try {
			User userUpdate = this.repository.findByid(id);
			if(Util.checkPass(user.getPassword(), userUpdate.getPassword())) {
				userUpdate.setId(id);
				userUpdate.setNombre(user.getNombre());
				userUpdate.setApellidos(user.getApellidos());
				userUpdate.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
				userUpdate = this.repository.save(userUpdate);
				return ResponseDto.success(userUpdate);
			} else {
				return ResponseDto.error("Constraseña incorrecta.");
			}
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}

	}

	@DeleteMapping(UserConstants.DELETE_REST + "/{id}")
	public ResponseDto<User> deleteUser(@PathVariable String id) {
		try {
			User userDelete = this.repository.findByid(id);
			userDelete.setEstado(false);
			this.repository.save(userDelete);
			return ResponseDto.success(userDelete);
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
}

class CountServices {
	public String idTipo;
	public int solicitudes;
}

class Exportar {
	public ArrayList<String> servicios;
	public ArrayList<Integer> numeros;
}
