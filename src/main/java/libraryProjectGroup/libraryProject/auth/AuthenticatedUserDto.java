package libraryProjectGroup.libraryProject.auth;

public class AuthenticatedUserDto {

    private final String username;
    private final String token;
    private final String roles;

    public AuthenticatedUserDto(String username, String token, String roles) {
        this.username = username;
        this.token = token;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getRoles() {
        return roles;
    }
}
