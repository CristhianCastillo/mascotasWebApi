package ga.mascotas.supplie.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ga.mascotas.supplie.util.SupplieConstants;
import ga.mascotas.supplie.model.Supplie;
import ga.mascotas.supplie.repository.SupplieRepository;
import ga.mascotas.supplieType.model.SupplieType;
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
public class SupplieController {
	@Autowired
	private SupplieRepository repository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@GetMapping(SupplieConstants.RETRIVE_REST + "/{idUsuario}")
	public ResponseDto<String> getSupplies(@PathVariable String idUsuario) {
		try {
			List<SupplieType> listaSuministros = this.mongoTemplate.findAll(SupplieType.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode suppliesUser = mapper.createArrayNode();
			for(int i = 0; i < listaSuministros.size(); i ++) {
				SupplieType tipoSuministroActual = listaSuministros.get(i);
				Query query = new Query();
				query.addCriteria(Criteria.where("idUsuario").is(idUsuario));
				query.addCriteria(Criteria.where("idTipo").is(tipoSuministroActual.getId()));
				List<Supplie> listaSuminsitrosFiltrados = this.mongoTemplate.find(query, Supplie.class);
				ObjectNode suministros = mapper.createObjectNode();
				JsonNode detalles = mapper.createArrayNode();
				for(int j = 0; j < listaSuminsitrosFiltrados.size(); j ++) {
					ObjectNode detalle = mapper.createObjectNode();
					Supplie suministroActual = listaSuminsitrosFiltrados.get(j);
					if(j == 0) {
						suministros.put("nombreTipoSuministro", tipoSuministroActual.getNombre());
					}
					detalle.put("id", suministroActual.getId());
					detalle.put("idTipo", suministroActual.getIdTipo());
					detalle.put("nombre", suministroActual.getNombre());
					detalle.put("cantidad", suministroActual.getCantidad());
					detalle.put("unidadMedida", suministroActual.getUnidadMedida());
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					detalle.put("fecha", dateFormat.format(suministroActual.getFecha()));
					detalle.put("precio", suministroActual.getPrecio());
					detalle.put("proveedor", suministroActual.getProveedor());
					detalle.put("comentario", suministroActual.getComentario());
					((ArrayNode) detalles).add(detalle);
				}
				if(detalles.size() > 0) {
					((ObjectNode) suministros).set("detalles", detalles);
					((ArrayNode) suppliesUser).add(suministros);
				}
			}
			return ResponseDto.success(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(suppliesUser));
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@PostMapping(SupplieConstants.CREATE_REST + "/{idUsuario}")
	public ResponseDto<Supplie> addSupplie(@PathVariable String idUsuario, @RequestBody Map<String, String> body) {
		try {
			String idTipo = body.get("idTipo");
			String nombre = body.get("nombre");
			String cantidadStr = body.get("cantidad");
			double cantidad = 0;
			try {
				cantidad = Double.parseDouble(cantidadStr);
			} catch (Exception ex) {
				cantidad = 0;
			}
			String unidad = body.get("unidad");
			String fechaStr = body.get("fechaCompra");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(fechaStr);
			double precio = 0;
			try {
				precio = Double.parseDouble(body.get("precio"));
			} catch(Exception ex) {
				precio = 0;
			}
			String proveedor = body.get("proveedor");
			String comentario = body.get("comentario");
			Supplie suministro = new Supplie();
			suministro.setId(ObjectId.get().toHexString());
			suministro.setIdUsuario(idUsuario);
			suministro.setIdTipo(idTipo);
			suministro.setNombre(nombre);
			suministro.setCantidad(cantidad);
			suministro.setUnidadMedida(unidad);
			suministro.setFecha(date);
			suministro.setPrecio(precio);
			suministro.setProveedor(proveedor);
			suministro.setComentario(comentario);
			Supplie result = this.repository.insert(suministro);
			return ResponseDto.success(result);
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@PutMapping(SupplieConstants.UPDATE_REST + "/{idSupplie}")
	public ResponseDto<Supplie> updateSupplie(@PathVariable String idSupplie, @RequestBody Map<String, String> body) {
		try {
			String idTipo = body.get("idTipo");
			String nombre = body.get("nombre");
			String cantidadStr = body.get("cantidad");
			double cantidad = 0;
			try {
				cantidad = Double.parseDouble(cantidadStr);
			} catch (Exception ex) {
				cantidad = 0;
			}
			String unidad = body.get("unidad");
			String fechaStr = body.get("fechaCompra");
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(fechaStr);
			double precio = 0;
			try {
				precio = Double.parseDouble(body.get("precio"));
			} catch(Exception ex) {
				precio = 0;
			}
			String proveedor = body.get("proveedor");
			String comentario = body.get("comentario");
			Supplie suministro = this.repository.findByid(idSupplie);
			suministro.setIdTipo(idTipo);
			suministro.setNombre(nombre);
			suministro.setCantidad(cantidad);
			suministro.setUnidadMedida(unidad);
			suministro.setFecha(date);
			suministro.setPrecio(precio);
			suministro.setProveedor(proveedor);
			suministro.setComentario(comentario);
			suministro = this.repository.save(suministro);
			return ResponseDto.success(suministro);
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@DeleteMapping(SupplieConstants.DELETE_REST + "/{idSupplie}")
	public ResponseDto deleteSupplie(@PathVariable String idSupplie) {
		try {
			this.repository.delete(repository.findByid(idSupplie));
			return ResponseDto.success();
		}
		catch(Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
}
