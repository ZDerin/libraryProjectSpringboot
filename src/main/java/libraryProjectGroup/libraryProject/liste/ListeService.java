package libraryProjectGroup.libraryProject.liste;

import libraryProjectGroup.libraryProject.buch.Buch;
import libraryProjectGroup.libraryProject.buch.BuchRepository;
import libraryProjectGroup.libraryProject.buch.BuchService;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.Book;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.BookRepository;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.BookServiceImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public List<Book> erstelleStandortListe(List<Book> wunschliste, String standort) throws IOException {
        List<Book> standortListe = new ArrayList<>();

        for (Book buch : wunschliste) {
            if (buch.getTid() == null) {
               buch.setTid(bookService.wandeleInTIDUm(buch.getIsbn()));
            }
            if (bookService.istVerfuegbar(buch.getTid(), standort)) {
                standortListe.add(buch);
            }
        }

        return standortListe;
    }
}
