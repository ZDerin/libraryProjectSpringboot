package libraryProjectGroup.libraryProject.zitate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZitatController {

    private final ZitatService zitatService;

    @Autowired
    public ZitatController(ZitatService zitatService) {
        this.zitatService = zitatService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/getZitat")
    public Zitat zeigeZufallsZitat() {
        System.out.println("Methode im Controller");
        return zitatService.getZufallsZitat();
    }
}
