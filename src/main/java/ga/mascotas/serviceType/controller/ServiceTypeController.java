package ga.mascotas.serviceType.controller;

import java.util.ArrayList;
import java.util.List;

import ga.mascotas.serviceType.util.ServiceTypeConstants;
import ga.mascotas.serviceType.model.ServiceType;
import ga.mascotas.serviceType.repository.ServiceTypeRepository;
import ga.mascotas.util.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ServiceTypeController {
	@Autowired
	private ServiceTypeRepository repository;
	
	@GetMapping(ServiceTypeConstants.RETRIVE_OWNER_REST)
	public ResponseDto<List<String>> getServiceTypesOwner() {
		try {
			List<ServiceType> listaServicios = this.repository.findAll();
			List<String> serviciosStr = new ArrayList<String>();
			for(int i = 0; i < listaServicios.size(); i ++) {
				serviciosStr.add(listaServicios.get(i).getNombre());
			}
			return ResponseDto.success(serviciosStr);
		} catch (Exception ex){
			return ResponseDto.error(ex.getMessage());
		}
	}
	
	@GetMapping(ServiceTypeConstants.RETRIVE_REST)
	public ResponseDto<List<ServiceType>> getServiceTypes() {
		try {
			return ResponseDto.success(this.repository.findAll());
		} catch (Exception ex){
			return ResponseDto.error(ex.getMessage());
		}
	}
}
