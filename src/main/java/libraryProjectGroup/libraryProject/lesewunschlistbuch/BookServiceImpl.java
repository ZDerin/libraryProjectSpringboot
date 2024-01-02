package libraryProjectGroup.libraryProject.lesewunschlistbuch;

import libraryProjectGroup.libraryProject.buch.Buch;
import libraryProjectGroup.libraryProject.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book buchSpeichern(Book buch) {
        return bookRepository.save(buch);
    }

    @Override
    public String erstelleCoverbildLink(String isbn) throws IOException {
        String hugendubel = "https://www.hugendubel.info/annotstream/" + isbn + "/COP";
        String openlibrary = "https://covers.openlibrary.org/b/isbn/" + isbn + ".jpg";
//        String ekz = "https://cover.ekz.de/" + isbn;      // braucht Style Information

        URL hugendubelURL = new URL(hugendubel);
        Scanner hugendubelScanner = new Scanner(hugendubelURL.openStream());

        if (Objects.equals(hugendubelScanner.next(), "<html style=\"height: 100%;\">")) {

//        if (isbn.startsWith("9783")) {    // Alternative, die ggf. weniger zuverlässig, aber schneller wäre
            return hugendubel;
        } else {
            return openlibrary;
        }

        // Default-Bild
    }

    @Override
    public String wandeleInTIDUm(String isbn) throws IOException {
        String tid = "";

        String suchUrl = "https://www.buecherhallen.de/katalog-suchergebnisse.html?suchbegriff=" + isbn;
        URL url = new URL(suchUrl);
        String website;

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder stringBuilder = new StringBuilder();
        while(scanner.hasNext()) {
            stringBuilder.append(scanner.next());
        }
        website = stringBuilder.toString();
        Pattern pattern = Pattern.compile("/medium/([\\w]+)\\.html");
        Matcher matcher = pattern.matcher(website);
        matcher.find();
        tid = matcher.group(1);
        return tid;
    }


    @Override
    public boolean istVerfuegbar(String gesuchteTID, String standort) throws IOException {
        String urlBasis = "https://www.buecherhallen.de/suchergebnis-detail/medium/";
        String urlBuch = urlBasis + gesuchteTID + ".html";
        URL url = new URL(urlBuch);
        String website;

        Scanner scanner = new Scanner(url.openStream());
        StringBuilder stringBuilder = new StringBuilder();
        while(scanner.hasNext()) {
            stringBuilder.append(scanner.next());
        }
        website = stringBuilder.toString();

        Pattern pattern = Pattern.compile("<.*location\">" + standort + "<[^>]*>[^VN]*Verfügbar.*");
        Matcher matcher = pattern.matcher(website);

        return matcher.find();
    }


    @Override
    public boolean findByIsbnAndUser(Book book) {
        List<Book> booksWithIsbnInRepo = bookRepository.findAll().stream()
                .filter(bookFromRepo -> (bookFromRepo.getIsbn().equals(book.getIsbn())) && (bookFromRepo.getUser() == book.getUser())).toList();
        return !booksWithIsbnInRepo.isEmpty();
    }
    @Override
    public void extractAndSaveBookData(String[] books, User user){
        for(String str : books){
            Book book = new Book();
            book.setUser(user);

            String isbn13 = "";
            String isbnMatch = "";
            String[] eachBookInArr = str.split(",");

            book.setTitle(eachBookInArr[1]);
            book.setAuthor(eachBookInArr[2]);

            List<String> isbnMatchList = Arrays.stream(eachBookInArr)
                    .filter(item -> item.matches("\\W*(978|979)\\d{10}\\W*"))
                    .toList();
            if(!isbnMatchList.isEmpty()){
                isbnMatch = isbnMatchList.get(0);
                int indexOfFirst9 = isbnMatch.indexOf('9');
                isbn13 = isbnMatch.substring(indexOfFirst9, indexOfFirst9 + 13);
                book.setIsbn(isbn13);
            } else {
                book.setIsbn("9780000000000"); // find sie!!!
            }
            if(!findByIsbnAndUser(book)){
                bookRepository.save(book);
            }
        }
    }

    @Override
    public void saveBookToReadingWishlist(BookCreationDto dto, User user) {
        Book book = new Book();
        book.setUser(user);
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());

        if(!findByIsbnAndUser(book)){
            bookRepository.save(book);
            System.out.println("in save book stage");
        }
    }

    @Override
    public List<Book> findAll(User user) {
        return bookRepository.findAll().stream().filter(book -> book.getUser() == user).toList();
    }
}
