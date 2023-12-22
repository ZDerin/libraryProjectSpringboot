package libraryProjectGroup.libraryProject.nutzer;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "nutzer")
public class Nutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nutzername")
    private String nutzername;

    @Column(name = "passwort")
    private String passwort;
}
