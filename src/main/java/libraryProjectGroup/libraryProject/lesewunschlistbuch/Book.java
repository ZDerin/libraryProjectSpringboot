package libraryProjectGroup.libraryProject.lesewunschlistbuch;


import jakarta.persistence.*;
import libraryProjectGroup.libraryProject.user.User;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name="buch")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @ElementCollection
    @CollectionTable(name = "book_isbns", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "book_isbn")
    private Collection<String> isbns = new HashSet<>();

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name = "coverbild")
    private String coverbild;

    @ElementCollection
    @CollectionTable(name = "book_tids", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "book_tid")
    private Collection<String> tids;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Book(){

    }

    public Book(Collection<String> isbns) throws IOException {
        this.isbns = isbns;   // ggf. Bindestriche ignorieren
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

    public Book(Collection<String> isbns, String title, String author, String coverbild) {
        this.isbns = isbns;
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

    public Collection<String> getIsbns() {
        return isbns;
    }

    public void setIsbns(Collection<String> isbn) {
        this.isbns = isbn;
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

    public Collection<String> getTids() {
        return tids;
    }

    public void setTids(Collection<String> tid) {
        this.tids = tid;
    }

    public String getCoverbild() {
        return coverbild;
    }

    public void setCoverbild(String coverbild) {
        this.coverbild = coverbild;
    }
}
