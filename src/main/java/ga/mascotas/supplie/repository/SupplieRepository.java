package ga.mascotas.supplie.repository;

import ga.mascotas.supplie.model.Supplie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplieRepository extends MongoRepository<Supplie, String> {
	public Supplie findByid(String id);
}
