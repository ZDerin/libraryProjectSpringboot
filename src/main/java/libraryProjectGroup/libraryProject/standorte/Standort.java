package libraryProjectGroup.libraryProject.standorte;

import jakarta.persistence.*;

@Entity
@Table(name="standort")
public class Standort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="name")
    private String name;


    public Standort() {
    }

    public Standort(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
