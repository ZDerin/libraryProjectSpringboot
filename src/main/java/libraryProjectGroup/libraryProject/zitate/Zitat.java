package libraryProjectGroup.libraryProject.zitate;

import jakarta.persistence.*;

@Entity
@Table(name = "zitate")
public class Zitat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name = "zitat")
    private String zitat;

    public Zitat() {

    }

    public Zitat(String name, String zitat) {
        this.name = name;
        this.zitat = zitat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZitat() {
        return zitat;
    }

    public void setZitat(String zitat) {
        this.zitat = zitat;
    }
}
