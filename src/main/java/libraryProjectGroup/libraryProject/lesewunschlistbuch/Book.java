package libraryProjectGroup.libraryProject.lesewunschlistbuch;


import jakarta.persistence.*;
import libraryProjectGroup.libraryProject.user.User;

import java.io.IOException;

@Entity
@Table(name="buch")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name= "isbn")
    private String isbn;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name = "coverbild")
    private String coverbild;

    @Column(name = "tid")
    private String tid;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Book(){

    }

    public Book(String isbn) throws IOException {
        this.isbn = isbn;   // ggf. Bindestriche ignorieren
//        this.titel = finde Titel und Autor über Amazon-API?;
//        this.autor = ;
        //this.coverbild = buchService.erstelleCoverbildLink(isbn);
        //this.tid = buchService.wandeleInTIDUm(isbn);
    }

//    public Buch(String titel, String autor) {
//        this.titel = titel;
//        this.autor = autor;
//        this.isbn = finde ISBN über Amazon-API?
//    }

    public Book(String isbn, String title, String author, String coverbild) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.coverbild = coverbild;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getCoverbild() {
        return coverbild;
    }




}
