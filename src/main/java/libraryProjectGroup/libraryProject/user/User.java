package libraryProjectGroup.libraryProject.user;

import jakarta.persistence.*;

import java.util.StringJoiner;
import java.util.UUID;

@Entity
@Table(name = "nutzer")
public class User {

    @Id
    private UUID id;
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", unique = true)
    private String password;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", unique = true)
    private Role role;

    public User(UUID id, String username, String password, String email, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User() {

    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("email='" + email + "'")
                .add("role=" + role)
                .toString();
    }
}

enum Role {
    ADMIN,
    USER
}

