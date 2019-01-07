package ga.mascotas.supplieType.controller;

import java.util.List;

import ga.mascotas.supplieType.util.SupplieTypeConstants;
import ga.mascotas.supplieType.model.SupplieType;
import ga.mascotas.supplieType.repository.SupplieTypeRepository;
import ga.mascotas.util.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class SupplieTypeController {
	@Autowired
	private SupplieTypeRepository repository;

	@GetMapping(SupplieTypeConstants.RETRIVE_REST)
	public ResponseDto<List<SupplieType>> getSupplieTypes() {
		try {
			return ResponseDto.success(this.repository.findAll());
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}

}
