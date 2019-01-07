package ga.mascotas.establishment.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ga.mascotas.establishment.util.EstablishmentConstants;
import ga.mascotas.establishment.model.Establishment;
import ga.mascotas.establishment.repository.EstablishmentRepository;
import ga.mascotas.util.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class EstablishmentController {
    @Autowired
    private EstablishmentRepository repository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping(EstablishmentConstants.RETRIVE_REST)
    public ResponseDto<List<Establishment>> getEstablishments() {
        try {
        return ResponseDto.success(this.mongoTemplate.find(Query.query(Criteria.where("estado").is(true)), Establishment.class));
        } catch (Exception ex) {
            return ResponseDto.error(ex.getMessage());
        }
    }

    @GetMapping(EstablishmentConstants.RETRIVE_REST + "/{idUsuario}")
    public ResponseDto<Establishment> getEstablishmentsUser(@PathVariable String idUsuario) {
        try {
            return ResponseDto.success(this.repository.findByIdUsuario(idUsuario));
        } catch (Exception ex){
            return ResponseDto.error(ex.getMessage());
        }
    }

    @PutMapping(EstablishmentConstants.QUALIFY_REST + "/{id}")
    public ResponseDto<Establishment> qualifyEstablishment(@PathVariable String id, @RequestBody Map<String, String> body) {
        try {
            String calificacion = body.get("calificacion");
            Establishment establecimiento = this.repository.findByid(id);
            if(calificacion.equals("BUENA"))
                establecimiento.setCalificacionesBuenas(1);
            else
                establecimiento.setCalificacionesMalas(1);
            Establishment result = this.repository.save(establecimiento);
            return ResponseDto.success(result);
        }
        catch(Exception ex) {
            return ResponseDto.error(ex.getMessage());
        }
    }

    @PutMapping(EstablishmentConstants.UPDATE_REST + "/{id}")
    public ResponseDto<Establishment> updateEstablishment(@PathVariable String id, @RequestBody Map<?, ?> body) {
        try {
            ArrayList<String> servicios = (ArrayList<String>)body.get("servicios");
            String nombre = (String)body.get("nombre");
            String telefono = (String)body.get("telefono");
            String direccion = (String)body.get("direccion");
            String email = (String)body.get("email");
            String paginaWeb = (String)body.get("paginaWeb");
            String horarios = (String)body.get("horarios");
            String horaInicial = (String)body.get("horaInicial");
            String horaFinal = (String)body.get("horaFinal");
            String descripcion = (String)body.get("descripcion");
            String imagen = (String)body.get("imagen");

            Establishment establecimiento = this.repository.findByid(id);
            establecimiento.setNombre(nombre);
            establecimiento.setTelefono(telefono);
            establecimiento.setDireccion(direccion);
            establecimiento.setServicios(servicios);
            establecimiento.setEmail(email);
            establecimiento.setPaginaWeb(paginaWeb);
            establecimiento.setHorarios(horarios);
            establecimiento.setHoraInicial(horaInicial);
            establecimiento.setHoraFinal(horaFinal);
            establecimiento.setDescripcion(descripcion);
            establecimiento.setImagen(imagen);
            Establishment result = this.repository.save(establecimiento);
            return ResponseDto.success(result);
        }
        catch(Exception ex) {
           return ResponseDto.error(ex.getMessage());
        }
    }

    @DeleteMapping(EstablishmentConstants.DELETE_REST + "/{id}")
    public ResponseDto<Establishment> deleteEstablishment(@PathVariable String id) {
        try {
            Establishment establecimiento = this.repository.findByid(id);
            establecimiento.setEstado(false);
            return ResponseDto.success(establecimiento);
        }
        catch(Exception ex) {
            return ResponseDto.error(ex.getMessage());
        }
    }
}
