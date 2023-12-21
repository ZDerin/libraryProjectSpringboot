package libraryProjectGroup.libraryProject.listen;

import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ListenController {

    // soll eine Liste übergeben, in der die am gewählten Standort verfügbaren Bucher
    // mit ihren Daten (Link zum Cover, Titel, Autor) aufgezählt sind

//    private final NutzerService nutzerService;
    private String standort;

    // for-Schleife für alle Bücher auf Wunschliste
    // Prüfung ob TID bereits in Datenbank hinterlegt (-> wandeleInTIDUm)
    // -> istVerfuegbar
    // hier entsteht ein Array mit verfügbaren Buch-Objekten
    // Array wird an Methode (wo?) weitergegeben, sodass am Ende ein
    // zweidimensionales Array mit [[Titel, Cover, Autor], [Titel, Cover, Autor], ... ]
    // ans Frontend weitergegeben werden kann

//    public String wandeleInTIDUm(Buch buch) {
//        String tid = "";
//        // Puppeteer Magic
//        return tid;
//    }




}
