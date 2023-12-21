package libraryProjectGroup.libraryProject.buch;

import jakarta.persistence.*;

@Entity
@Table(name = "buch")
public class Buch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "isbn")
    private String isbn;
    @Column(name = "titel")
    private String titel;
    @Column(name = "autor")
    private String autor;
    @Column(name = "coverbild")
    private String coverbild;
    @Column(name = "tid")
    private String tid;

    private BuchService buchService;


    public Buch(String isbn) {
        this.isbn = isbn;
//        this.titel = finde Titel und Autor über Amazon-API?;
//        this.autor = ;
        this.coverbild = "https://covers.openlibrary.org/b/isbn/" + isbn + ".jpg";
        this.tid = buchService.
    }

//    public Buch(String titel, String autor) {
//        this.titel = titel;
//        this.autor = autor;
//        this.isbn = finde ISBN über Amazon-API?
//    }


    public Buch(String isbn, String titel, String autor, String coverbild) {
        this.isbn = isbn;
        this.titel = titel;
        this.autor = autor;
        this.coverbild = coverbild;
    }

    public Buch() {

    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCoverbild() {
        return coverbild;
    }

    public void setCoverbild(String coverbild) {
        this.coverbild = coverbild;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
