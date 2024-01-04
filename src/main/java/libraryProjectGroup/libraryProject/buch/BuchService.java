package libraryProjectGroup.libraryProject.buch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class BuchService {

   private final BuchRepository buchRepository;

    @Autowired
    public BuchService(BuchRepository buchRepository) {
        this.buchRepository = buchRepository;
    }

    public Buch buchSpeichern(Buch buch) {
        return buchRepository.save(buch);
    }

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
