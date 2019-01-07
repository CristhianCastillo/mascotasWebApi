package ga.mascotas.supplieType.repository;

import ga.mascotas.supplieType.model.SupplieType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupplieTypeRepository extends MongoRepository<SupplieType, String> {

}
