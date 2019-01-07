package ga.mascotas.security.service;

import ga.mascotas.util.LoginResultDto;

public interface TokenService {

    LoginResultDto getToken(String username, String password) throws Exception;
}
