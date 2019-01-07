package ga.mascotas.agenda.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ga.mascotas.agenda.util.AgendaConstants;
import ga.mascotas.agenda.model.Agenda;
import ga.mascotas.agenda.repository.AgendaRepository;
import ga.mascotas.pet.model.Pet;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@CrossOrigin
public class AgendaController {
    @Autowired
    private AgendaRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping(AgendaConstants.RETRIVE_REST + "/{idUsuario}")
    public ResponseDto<String> getEvents(@PathVariable String idUsuario) {
        try {
            List<Pet> listaMascotasUsuario = this.mongoTemplate.find(Query.query(Criteria.where("idUsuario").is(idUsuario)), Pet.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode agendaUser = mapper.createArrayNode();
            for(int i = 0; i < listaMascotasUsuario.size(); i ++) {
                ObjectNode mascota = mapper.createObjectNode();
                mascota.put("id",  listaMascotasUsuario.get(i).getId());
                mascota.put("nombre",  listaMascotasUsuario.get(i).getNombre());

                JsonNode citasNode = mapper.createArrayNode();
                List<Agenda> listaCitas = this.mongoTemplate.find(Query.query(Criteria.where("idMascota").is(listaMascotasUsuario.get(i).getId())), Agenda.class);
                for(int j = 0; j < listaCitas.size(); j ++) {
                    ObjectNode cita = mapper.createObjectNode();
                    Agenda agenda = listaCitas.get(j);
                    cita.put("id", agenda.getId());
                    cita.put("nombre", agenda.getNombre());
                    cita.put("ubicacion", agenda.getUbicacion());
                    //ServiceType actividad = this.mongoTemplate.findOne(Query.query(Criteria.where("id").is(agenda.getIdTipo())), ServiceType.class);
                    cita.put("tipoActividad", agenda.getIdTipo());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    cita.put("fecha", dateFormat.format(agenda.getFecha()));
                    cita.put("descripcionEvento", agenda.getDescripcion());
                    ((ArrayNode) citasNode).add(cita);
                }
                ((ObjectNode) mascota).set("citas", citasNode);
                ((ArrayNode) agendaUser).add(mascota);
            }
            return ResponseDto.success(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(agendaUser));
        } catch(Exception ex) {
            return ResponseDto.error(ex.getMessage());
        }
    }

    @PostMapping(AgendaConstants.CREATE_REST)
    public ResponseDto<Agenda> addEvent(@RequestBody Map<String, String> body) {
        try {
            Agenda agenda = new Agenda();
            String idMascota = body.get("idMascota");
            String nombre = body.get("nombre");
            String ubicacion = body.get("ubicacion");
            String idTipo = body.get("idTipo");
            String fechaStr = body.get("fecha");
            String descripcion = body.get("descripcion");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = format.parse(fechaStr);

            agenda.setId(ObjectId.get().toHexString());
            agenda.setIdMascota(idMascota);
            agenda.setIdTipo(idTipo);
            agenda.setNombre(nombre);
            agenda.setUbicacion(ubicacion);
            agenda.setFecha(date);
            agenda.setDescripcion(descripcion);
            Agenda result = this.repository.insert(agenda);
            return ResponseDto.success(result);
        } catch (Exception ex) {
            return ResponseDto.error(ex.getMessage());
        }
    }

    @PutMapping(AgendaConstants.UPDATE_REST + "/{id}")
    public ResponseDto<Agenda> updateEvent(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            Agenda agenda = this.repository.findByid(id);
            String idMascota = body.get("idMascota");
            String nombre = body.get("nombre");
            String ubicacion = body.get("ubicacion");
            String idTipo = body.get("idTipo");
            String fechaStr = body.get("fecha");
            String descripcion = body.get("descripcion");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date = format.parse(fechaStr);

            agenda.setIdMascota(idMascota);
            agenda.setIdTipo(idTipo);
            agenda.setNombre(nombre);
            agenda.setUbicacion(ubicacion);
            agenda.setFecha(date);
            agenda.setDescripcion(descripcion);
            Agenda result = this.repository.save(agenda);
            return ResponseDto.success(result);
        } catch(Exception ex) {
            return ResponseDto.error(ex.getMessage());
        }

    }

    @DeleteMapping(AgendaConstants.DELETE_REST + "/{id}")
    public ResponseDto<Agenda> deleteEvent(@PathVariable String id) {
        try {
           this.repository.delete(repository.findByid(id));
           return ResponseDto.success();
        }
        catch(Exception ex) {
            return ResponseDto.error(ex.getMessage());
        }
    }
}
