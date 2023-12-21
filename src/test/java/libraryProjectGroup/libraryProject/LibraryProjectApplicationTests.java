package libraryProjectGroup.libraryProject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LibraryProjectApplicationTests {

	@Test
	void contextLoads() {
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

		Pattern pattern = Pattern.compile("<.*" + standort + "<[^>]*>[^V]*Verfügbar.*");
		Matcher matcher = pattern.matcher(website);

		return matcher.find();
	}

	@Test
	void istVerfuegbar() throws IOException {
		assertTrue(istVerfuegbar("T02325615X", "Zentralbibliothek"));
		assertTrue(istVerfuegbar("T020329445", "Wandsbek"));
	}

}
