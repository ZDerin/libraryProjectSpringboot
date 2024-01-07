package libraryProjectGroup.libraryProject.standorte;

import libraryProjectGroup.libraryProject.lesewunschlistbuch.Book;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.BookFrontendDto;
import libraryProjectGroup.libraryProject.lesewunschlistbuch.BookServiceImpl;
import libraryProjectGroup.libraryProject.liste.ListeService;
import libraryProjectGroup.libraryProject.user.User;
import libraryProjectGroup.libraryProject.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class StandortController {

    private final StandortService standortService;
    private final ListeService listeService;

    @Autowired
    public StandortController(StandortService standortService,
                              ListeService listeService) {
        this.standortService = standortService;
        this.listeService = listeService;
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/getLocations")
    public List<Standort> zeigeStandorte() {
        return standortService.erstelleListeAllerStandorte();
    }

    /*unnötig, weil als requestparam gut funktioniert
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value= "/changeLocation")
    public void uploadReadingWishlist(@RequestBody String location) {
        listeService.setStandort(location);
    }*/

}
