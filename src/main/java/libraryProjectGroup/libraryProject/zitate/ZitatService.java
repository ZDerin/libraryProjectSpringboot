package libraryProjectGroup.libraryProject.zitate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ZitatService {

    private final ZitatRepository zitatRepository;

    public ZitatService(ZitatRepository zitatRepository) {
        this.zitatRepository = zitatRepository;
    }

    public Zitat getZufallsZitat() {
        long gesamtzahl = zitatRepository.count();
        int zufallszahl = (int)(Math.random() * gesamtzahl);
        Page<Zitat> zitatPage = zitatRepository.findAll(new PageRequest(zufallszahl, 1));
        Zitat zitat = null;
        if (zitatPage.hasContent()) {
            zitat = zitatPage.getContent().get(0);
        }
        return zitat;
    }
}
