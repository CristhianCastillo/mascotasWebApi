package ga.mascotas.request.repository;

import java.util.Date;
import java.util.List;

import ga.mascotas.request.model.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestRepository extends MongoRepository<Request, String>{
	public Request findByid(String id);
	public List<Request> findByIdEstablecimiento(String idEstablecimiento);
	public List<Request> findByIdMascota(String idMascota);
	public List<Request> findByFechaBetween(Date fechaGT, Date fechaLT);
	public Request findFirstByOrderByFechaDesc();
}
