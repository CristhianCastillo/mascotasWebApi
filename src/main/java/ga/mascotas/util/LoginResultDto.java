package ga.mascotas.util;

public class LoginResultDto {
    private String root;
    private String token;
    private String user;
    private String message;

    public LoginResultDto() {
    }

    public LoginResultDto(String root, String token, String user, String message) {
        this.root = root;
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
