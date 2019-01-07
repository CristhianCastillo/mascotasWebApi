package ga.mascotas.establishment.repository;

import ga.mascotas.establishment.model.Establishment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstablishmentRepository extends MongoRepository<Establishment, String> {
	public Establishment findByid(String id);
	public Establishment findByNombre(String nombre);
	public Establishment findByNit(String nit);
	public Establishment findByIdUsuario(String idUsuario);
}
