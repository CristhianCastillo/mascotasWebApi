package ga.mascotas.pet.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ga.mascotas.agenda.model.Agenda;
import ga.mascotas.pet.util.PetConstants;
import ga.mascotas.pet.model.Pet;
import ga.mascotas.pet.repository.PetRepository;
import ga.mascotas.util.ResponseDto;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@CrossOrigin
public class PetController {
	@Autowired
	private PetRepository repository;
	@Autowired
	private MongoTemplate mongoTemplate;

	@GetMapping(PetConstants.RETRIVE_REST)
	public List<Pet> getPets() throws IOException {
		return this.repository.findAll();
	}
	
	@GetMapping(PetConstants.RETRIVE_REST + "/{idUsuario}")
	public ResponseDto<List<Pet>> getPetsUser(@PathVariable String idUsuario){
		try {
			return ResponseDto.success(this.repository.findByIdUsuario(idUsuario));
		} catch (Exception ex){
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@PostMapping(PetConstants.CREATE_REST)
	public ResponseDto<Pet> addPet(@RequestBody Map<String, String> body) {
		try {
			Pet petInsert = new Pet();
			petInsert.setId(ObjectId.get().toHexString());
			String idUsuario = body.get("idUsuario");
			String imagen = body.get("imagen");
			String nombre = body.get("nombre");
			String idTipo = body.get("idTipo");
			String genero = body.get("genero");
			String fechaStr = body.get("fechaNacimiento");
			String raza = body.get("raza");
			String esterilizado = body.get("esterilizado");
			String color = body.get("color");
			String descripcion = body.get("descripcion");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(fechaStr);
			System.out.println("-------- Fecha Nacimiento --------------");
			System.out.println(date);
			if(esterilizado.equals("Si"))
				petInsert.setEsterilizado(true);
			else
				petInsert.setEsterilizado(false);
			petInsert.setIdUsuario(idUsuario);
			petInsert.setImagen(imagen);
			petInsert.setNombre(nombre);
			petInsert.setIdTipo(idTipo);
			petInsert.setGenero(genero);
			petInsert.setFechaNacimiento(date);
			petInsert.setRaza(raza);
			petInsert.setColor(color);
			petInsert.setDescripcion(descripcion);
			Pet result = this.repository.insert(petInsert);
			return ResponseDto.success(result);
		}
		catch(Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}

	@PutMapping(PetConstants.UPDATE_REST + "/{id}")
	public ResponseDto<Pet> updatePet(@PathVariable String id, @RequestBody Map<String, String> body) {
		try {
			Pet petUpdate = this.repository.findByid(id);
			String imagen = body.get("imagen");
			String nombre = body.get("nombre");
			String idTipo = body.get("idTipo");
			String genero = body.get("genero");
			String fechaStr = body.get("fechaNacimiento");
			String raza = body.get("raza");
			String esterilizado = body.get("esterilizado");
			String color = body.get("color");
			String descripcion = body.get("descripcion");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(fechaStr);
			System.out.println("-------- Fecha Nacimiento --------------");
			System.out.println(date);
			if(esterilizado.equals("Si"))
				petUpdate.setEsterilizado(true);
			else
				petUpdate.setEsterilizado(false);
			petUpdate.setImagen(imagen);
			petUpdate.setNombre(nombre);
			petUpdate.setIdTipo(idTipo);
			petUpdate.setGenero(genero);
			petUpdate.setFechaNacimiento(date);
			petUpdate.setRaza(raza);
			petUpdate.setColor(color);
			petUpdate.setDescripcion(descripcion);
			Pet result = this.repository.save(petUpdate);
			return ResponseDto.success(result);
		}
		catch(Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}

	@DeleteMapping(PetConstants.DELETE_REST + "/{id}")
	public ResponseDto<Pet> deletePet(@PathVariable String id) {
		try {
			this.mongoTemplate.findAllAndRemove(Query.query(Criteria.where("idMascota").is(id)), Agenda.class);
			Pet result = repository.findByid(id);
			this.repository.delete(result);
			return ResponseDto.success(result);
		}
		catch(Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
}
