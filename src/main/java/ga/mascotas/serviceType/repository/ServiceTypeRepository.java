package ga.mascotas.serviceType.repository;

import ga.mascotas.serviceType.model.ServiceType;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServiceTypeRepository extends MongoRepository<ServiceType, String>{

}
