package libraryProjectGroup.libraryProject.lesewunschlistbuch;

import libraryProjectGroup.libraryProject.buch.Buch;
import libraryProjectGroup.libraryProject.user.User;
import libraryProjectGroup.libraryProject.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
public class BookController {

    private BookServiceImpl bookServiceImpl;
    private UserRepository userRepository;
    @Autowired
    public BookController(BookServiceImpl bookServiceImpl, UserRepository userRepository) {
        this.bookServiceImpl = bookServiceImpl;
        this.userRepository = userRepository;
    }

    @PostMapping("/speichern")
    public Book speichereBuch(@RequestBody Book buch) {
        return bookServiceImpl.buchSpeichern(buch);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value= "/readingListImport")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadReadingWishlist(@RequestBody String[] books, Principal principal) throws IOException {
        System.out.println("in uploadReadingWishlist Func in Controller");
        User user = userRepository.findByUsername(principal.getName());
        bookServiceImpl.extractAndSaveBookData(books, user);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value= "/addBookToRead")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBookToWishlist(@RequestBody BookCreationDto book, Principal principal){
        System.out.println("in addBookToWishList");
        User user = userRepository.findByUsername(principal.getName());
        bookServiceImpl.saveBookToReadingWishlist(book, user);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value= "/showBooksToRead")
    @ResponseStatus(HttpStatus.CREATED)
    public void showBooksToRead(Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        List<Book> readingWishlist = bookServiceImpl.findAll(user);
    }
}