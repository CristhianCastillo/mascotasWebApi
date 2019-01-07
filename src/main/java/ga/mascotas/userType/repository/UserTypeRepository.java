package ga.mascotas.userType.repository;

import ga.mascotas.userType.model.UserType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTypeRepository extends MongoRepository<UserType, String> {
	public UserType findByNombre(String nombre);
}
