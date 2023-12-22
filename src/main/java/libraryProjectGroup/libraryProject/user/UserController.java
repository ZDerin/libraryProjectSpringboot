package libraryProjectGroup.libraryProject.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(
        value = "/api/v1/users"
)
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID register(@RequestBody UserCreationDto dto) {
        User createdUser = userRepository.save(
                new User(
                        UUID.randomUUID(),
                        dto.getUsername(),
                        bCryptPasswordEncoder.encode(dto.getPassword()),
                        dto.getEmail(),
                        Role.USER
                )
        );
        return createdUser.getId();
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping()
    public List<String> findAll() {
        return userRepository.findAll().stream()
                .map(User::toString)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable UUID id) {
        userRepository.deleteById(id);
    }

//    @PreAuthorize("hasAuthority('ROLE_SELLER')")
//    @PutMapping()
//    public UserDto updateUser(@RequestBody String username, Principal principal) {
//        User user = userRepository.findByUsername(principal.getName());
//        if (user != null) {
//            User updateUser = user.withUsername(username);
//            return UserDto.fromUser(userRepository.save(updateUser));
//        } else {
//            throw new NoDocumentFoundException("No user found with username " + principal.getName());
//        }
//    }
}
