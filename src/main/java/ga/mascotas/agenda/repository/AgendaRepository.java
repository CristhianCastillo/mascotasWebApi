package ga.mascotas.agenda.repository;

import ga.mascotas.agenda.model.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AgendaRepository extends MongoRepository<Agenda, String> {
    public Agenda findByid(String id);
}
