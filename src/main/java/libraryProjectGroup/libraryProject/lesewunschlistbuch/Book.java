package libraryProjectGroup.libraryProject.lesewunschlistbuch;


import jakarta.persistence.*;
import libraryProjectGroup.libraryProject.user.User;

@Entity
@Table(name="lesewunschlistbuch")
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

    @ManyToOne
    @JoinColumn(name="user_id") // username better???????????
    private User user;

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


}
