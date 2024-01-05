package libraryProjectGroup.libraryProject.lesewunschlistbuch;

import libraryProjectGroup.libraryProject.user.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface BookService {

    Book buchSpeichern(Book buch);
    String erstelleCoverbildLink(Set<String> isbnListe) throws IOException;

    String wandeleInTIDUm(String isbn) throws IOException;

    boolean istVerfuegbar(String gesuchteTID, String standort) throws IOException;
    boolean findByIsbnAndUser(Book book);
    void extractAndSaveBookData(String[] books, User user) throws IOException;
    void saveBookToReadingWishlist(BookCreationDto book, User user);

    List<Book> findAll(User user);


}
