package ga.mascotas.petType.controller;

import java.util.List;

import ga.mascotas.petType.util.PetTypeConstants;
import ga.mascotas.petType.model.PetType;
import ga.mascotas.petType.repository.PetTypeRepository;
import ga.mascotas.util.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class PetTypeController {
	@Autowired
	private PetTypeRepository repository;
	
	@GetMapping(PetTypeConstants.RETRIVE_REST)
	public ResponseDto<List<PetType>> getPetTypes() {
		try {
			return ResponseDto.success(this.repository.findAll());
		} catch (Exception ex) {
			return ResponseDto.error(ex.getMessage());
		}
	}
}
