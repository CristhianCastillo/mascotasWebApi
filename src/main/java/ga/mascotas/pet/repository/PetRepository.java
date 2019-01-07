package ga.mascotas.pet.repository;

import java.util.List;
import ga.mascotas.pet.model.Pet;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PetRepository extends MongoRepository<Pet, String> {
	public Pet findByNombre(String nombre);
	public List<Pet> findByGenero(String genero);
	public Pet findByid(String id);
	public List<Pet> findByIdUsuario(String idUsuario);
}
