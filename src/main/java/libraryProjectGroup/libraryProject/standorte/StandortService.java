package libraryProjectGroup.libraryProject.standorte;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StandortService {

private final StandortRepository standortRepository;

    public StandortService(StandortRepository standortRepository) {
        this.standortRepository = standortRepository;
    }

    public List<Standort> erstelleListeAllerStandorte() {
        return standortRepository.findAll();
    }
}
