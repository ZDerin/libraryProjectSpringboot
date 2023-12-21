package libraryProjectGroup.libraryProject.buch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuchService {

    @Autowired
    public BuchService() {
    }

    public String erstelleCoverbildLink(String isbn) {
        return "https://covers.openlibrary.org/b/isbn/" + isbn + ".jpg";
    }

    public String wandeleInTIDUm(String isbn) {
        String tid = "";
        return tid;
    }
}
