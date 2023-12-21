package libraryProjectGroup.libraryProject.liste;

import libraryProjectGroup.libraryProject.buch.Buch;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class ListenService {

    // soll eine Liste übergeben, in der die am gewählten Standort verfügbaren Bucher
    // mit ihren Daten (Link zum Cover, Titel, Autor) aufgezählt sind

    //    private final NutzerService nutzerService;
    private String standort;

    public ListenService() {
    }
    // for-Schleife für alle Bücher auf Wunschliste
    // Prüfung ob TID bereits in Datenbank hinterlegt (-> wandeleInTIDUm)
    // -> istVerfuegbar
    // hier entsteht ein Array mit verfügbaren Buch-Objekten
    // Array wird an Methode (wo?) weitergegeben, sodass am Ende ein
    // zweidimensionales Array mit [[Titel, Cover, Autor], [Titel, Cover, Autor], ... ]
    // ans Frontend weitergegeben werden kann

    public String wandeleInTIDUm(String isbn) throws IOException {
//        String isbn = Buch.getIsbn(); --> irgendwann muss die Methode wieder ein Buchobjekt bekommen
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
}
