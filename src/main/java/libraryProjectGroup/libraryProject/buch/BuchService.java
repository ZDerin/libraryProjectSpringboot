package libraryProjectGroup.libraryProject.buch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

@Service
public class BuchService {

    @Autowired
    public BuchService() {
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

    public String wandeleInTIDUm(String isbn) {
        String tid = "";
        return tid;
    }
}
