package libraryProjectGroup.libraryProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryProjectApplication.class, args);
	}

}

/*keytool -genkey -alias jwt-signing -keyalg RSA -keystore backend-auth.jks -keysize 2048*/