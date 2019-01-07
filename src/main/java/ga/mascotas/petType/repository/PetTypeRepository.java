package ga.mascotas.petType.repository;

import ga.mascotas.petType.model.PetType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PetTypeRepository extends MongoRepository<PetType, String>{

}
