package libraryProjectGroup.libraryProject.liste;

import libraryProjectGroup.libraryProject.lesewunschlistbuch.Book;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.BookFrontendDto;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.BookRepository;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.BookServiceImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListeService {
    //ist es sinnvoll, eine Instanz des buchRepository als Klassenvariable zu deklarieren?
    //muss so eine Instanz auch in den Konstruktor?
    private final BookRepository bookRepository;
    private final BookServiceImpl bookService;

    // soll eine Liste übergeben, in der die am gewählten Standort verfügbaren Bucher
    // mit ihren Daten (Link zum Cover, Titel, Autor) aufgezählt sind

    //    private final NutzerService nutzerService;
    private String standort;

    public ListeService(BookRepository bookRepository, BookServiceImpl bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }
    // for-Schleife für alle Bücher auf Wunschliste
    // Prüfung ob TID bereits in Datenbank hinterlegt (-> wandeleInTIDUm)
    // -> istVerfuegbar
    // hier entsteht ein Array mit verfügbaren Buch-Objekten
    // Array wird an Methode (wo?) weitergegeben, sodass am Ende ein
    // zweidimensionales Array mit [[Titel, Cover, Autor], [Titel, Cover, Autor], ... ]
    // ans Frontend weitergegeben werden kann

    public List<BookFrontendDto> erstelleStandortListe(List<Book> wunschliste, String standort) {
        List<BookFrontendDto> standortListe = new ArrayList<>();

        for (Book buch : wunschliste) {
            //List<String> isbns = (List<String>) buch.getIsbns();
            List<String> tids = (List<String>) buch.getTids();

            /*
            if(!isbns.isEmpty()){
                for (String isbn : isbns) {
                    tids.add(bookService.wandeleInTIDUm(isbn));
                }
            }*/

            if(!tids.isEmpty()){
                for (String tid : tids) {
                    if (bookService.istVerfuegbar(tid, standort)) {
                        BookFrontendDto dto = new BookFrontendDto();
                        dto.setTitle(buch.getTitle());
                        dto.setAuthor(buch.getAuthor());
                        dto.setCoverbild(buch.getCoverbild());
                        dto.setStandortLink("https://www.buecherhallen.de/suchergebnis-detail/medium/" + tid + ".html");
                        standortListe.add(dto);
                        break;
                    }
                }
            }
        }

        return standortListe;
    }

    public List<BookFrontendDto> findAllAsDTO(List<Book> readingWishlist){
        List<BookFrontendDto> allBooksAsDto = new ArrayList<>();

        for (Book book : readingWishlist) {
            BookFrontendDto dto = new BookFrontendDto();
            dto.setTitle(book.getTitle());
            dto.setAuthor(book.getAuthor());
            dto.setCoverbild(book.getCoverbild());
            allBooksAsDto.add(dto);
        }
        return allBooksAsDto;
    }
}

