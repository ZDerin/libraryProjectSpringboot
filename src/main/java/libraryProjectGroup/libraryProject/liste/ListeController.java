package libraryProjectGroup.libraryProject.liste;

import libraryProjectGroup.libraryProject.lesewunschlistbuch.Book;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.BookFrontendDto;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.BookServiceImpl;
import libraryProjectGroup.libraryProject.user.User;
import libraryProjectGroup.libraryProject.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class ListeController {

    private BookServiceImpl bookServiceImpl;
    private UserRepository userRepository;

    private ListeService listeService;
    @Autowired
    public ListeController(BookServiceImpl bookServiceImpl, UserRepository userRepository, ListeService listeService) {
        this.bookServiceImpl = bookServiceImpl;
        this.userRepository = userRepository;
        this.listeService = listeService;
    }
    // soll eine Liste übergeben, in der die am gewählten Standort verfügbaren Bucher
    // mit ihren Daten (Link zum Cover, Titel, Autor) aufgezählt sind


    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value= "/showAvailableBooks")
    public List<BookFrontendDto> showBooksToRead(Principal principal){
        User user = userRepository.findByUsername(principal.getName());
        List<Book> readingWishlist = bookServiceImpl.findAll(user);
        List<BookFrontendDto> gefilterteWunschliste =  listeService.erstelleStandortListe(readingWishlist, "Zentralbibliothek");
        System.out.println(gefilterteWunschliste);
        return gefilterteWunschliste;
    }
}
