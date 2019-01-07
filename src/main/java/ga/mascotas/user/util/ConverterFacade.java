package ga.mascotas.user.util;


import ga.mascotas.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ConverterFacade {

    @Autowired
    private ConverterFactory converterFactory;

    public User convert(final User dto) {
        return (User) converterFactory.getConverter(dto.getClass()).convert(dto);
    }
}
