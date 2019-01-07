package ga.mascotas.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ga.mascotas.user.model.User;
import ga.mascotas.user.repository.UserRepository;
import ga.mascotas.userType.model.UserType;
import ga.mascotas.util.LoginResultDto;
import ga.mascotas.util.Util;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


@Service
public class JsonWebTokenService implements TokenService {

    private static int tokenExpirationTime = 30;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${security.token.secret.key}")
    private String tokenKey;

    @Autowired
    private UserRepository repository;

    private final UserDetailsService userDetailsService;

    @Autowired
    public JsonWebTokenService(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public LoginResultDto getToken(final String username, final String password) throws Exception {
        if (username == null || password == null) {
            return null;
        }
        final User user = repository.findByUsername(username);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode resultNode = mapper.createObjectNode();
        if(user == null) {
            return null;
        }
        else {
            if(Util.checkPass(password, user.getPassword())) {
                UserType tipoUsuario = this.mongoTemplate.findOne(Query.query(Criteria.where("_id").is(user.getIdTipo())), UserType.class);
                Map<String, Object> tokenData = new HashMap<>();
                Calendar calendar = Calendar.getInstance();
                JwtBuilder jwtBuilder = Jwts.builder();
                LoginResultDto result = new LoginResultDto();
                switch (tipoUsuario.getNombre()) {
                    case "Usuario":
                        tokenData.put("clientType", "user");
                        tokenData.put("userID", user.getId());
                        tokenData.put("username", user.getUsername());
                        tokenData.put("token_create_date", LocalDateTime.now());
                        calendar.add(Calendar.MINUTE, tokenExpirationTime);
                        tokenData.put("token_expiration_date", calendar.getTime());
                        jwtBuilder.setExpiration(calendar.getTime());
                        jwtBuilder.setClaims(tokenData);
                        String tokenUsuario = jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenKey).compact();

                        result.setRoot("Usuario");
                        result.setToken(tokenUsuario);
                        result.setUser(user.getId());
                        result.setMessage("OK");
                        return result;
                    case "Propietario":
                        tokenData.put("clientType", "user");
                        tokenData.put("userID", user.getId());
                        tokenData.put("username", user.getUsername());
                        tokenData.put("token_create_date", LocalDateTime.now());
                        calendar.add(Calendar.MINUTE, tokenExpirationTime);
                        tokenData.put("token_expiration_date", calendar.getTime());
                        jwtBuilder.setExpiration(calendar.getTime());
                        jwtBuilder.setClaims(tokenData);
                        String tokenPropietario = jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenKey).compact();
                        result.setRoot("Propietario");
                        result.setToken(tokenPropietario);
                        result.setUser(user.getId());
                        result.setMessage("OK");
                        return result;
                    default:
                        return null;
                }
            }
            else {
                return null;
            }
        }
    }

    public static void setTokenExpirationTime(final int tokenExpirationTime) {
        JsonWebTokenService.tokenExpirationTime = tokenExpirationTime;
    }
}
