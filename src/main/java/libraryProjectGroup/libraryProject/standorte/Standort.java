package libraryProjectGroup.libraryProject.standorte;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Standort {
    private String name;

    @Id
    private Long id;

    public Standort() {
    }

    public Standort(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
