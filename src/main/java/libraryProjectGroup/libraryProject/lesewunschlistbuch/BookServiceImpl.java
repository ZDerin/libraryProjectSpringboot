package libraryProjectGroup.libraryProject.lesewunschlistbuch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import libraryProjectGroup.libraryProject.buch.Buch;
import libraryProjectGroup.libraryProject.user.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLOutput;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book buchSpeichern(Book buch) {
        return bookRepository.save(buch);
    }

    @Override
    public String erstelleCoverbildLink(String isbn) throws IOException {
        String hugendubel = "https://www.hugendubel.info/annotstream/" + isbn + "/COP";
        String openlibrary = "https://covers.openlibrary.org/b/isbn/" + isbn + ".jpg";
//        String ekz = "https://cover.ekz.de/" + isbn;      // braucht Style Information
/*
        URL hugendubelURL = new URL(hugendubel);
        Scanner hugendubelScanner = new Scanner(hugendubelURL.openStream());

        if (Objects.equals(hugendubelScanner.next(), "<html style=\"height: 100%;\">")) {

//        if (isbn.startsWith("9783")) {    // Alternative, die ggf. weniger zuverlässig, aber schneller wäre
            return hugendubel;
        } else {
            return openlibrary;
        }*/
        return "https://openlibrary.org/works/OL82563W/Harry_Potter_and_the_Philosopher%27s_Stone?edition=key%3A/books/OL39793700M";
        // Default-Bild
    }

    @Override
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
        if(matcher.find()){
            tid = matcher.group(1);
        }
        return tid;
    }


    @Override
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


    @Override
    public boolean findByIsbnAndUser(Book book) {
        List<Book> booksWithIsbnInRepo = bookRepository.findAll().stream()
                .filter(bookFromRepo -> (bookFromRepo.getIsbn().equals(book.getIsbn())) && (bookFromRepo.getUser() == book.getUser())).toList();
        return !booksWithIsbnInRepo.isEmpty();
    }
    @Override
    public void extractAndSaveBookData(String[] books, User user) throws IOException{
        for(String str : books){
            Book book = new Book();
            book.setUser(user);

            String isbn13 = "";
            String isbnMatch = "";
            String[] eachBookInArr = str.split(",");

            String[] titleArr = eachBookInArr[1].split(" ");
            //String titleA = String.join("%20", titleArr);

            String encodedTitle = URLEncoder.encode(eachBookInArr[1], StandardCharsets.UTF_8.toString());
            String encodedAuthor = URLEncoder.encode(eachBookInArr[2], StandardCharsets.UTF_8.toString());

            String[] authorArr = eachBookInArr[2].split(" ");
           // String authorA = String.join("%20", authorArr);

            findIsbns( encodedTitle,  encodedAuthor);


            book.setTitle(eachBookInArr[1]);
            book.setAuthor(eachBookInArr[2]);

            List<String> isbnMatchList = Arrays.stream(eachBookInArr)
                    .filter(item -> item.matches("\\W*(978|979)\\d{10}\\W*"))
                    .toList();
            if(!isbnMatchList.isEmpty()){
                isbnMatch = isbnMatchList.get(0);
                int indexOfFirst9 = isbnMatch.indexOf('9');
                isbn13 = isbnMatch.substring(indexOfFirst9, indexOfFirst9 + 13).trim();
                book.setIsbn(isbn13);
                book.setTid(wandeleInTIDUm(isbn13));
                book.setCoverbild(erstelleCoverbildLink(isbn13));
            } else {
                book.setIsbn("9780000000000"); // find sie!!!
            }
            if(!findByIsbnAndUser(book) && !isbnMatchList.isEmpty()){
                bookRepository.save(book);
            }
        }
    }

    @Override
    public void saveBookToReadingWishlist(BookCreationDto dto, User user) {
        Book book = new Book();
        book.setUser(user);
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());

        if(!findByIsbnAndUser(book)){
            bookRepository.save(book);
        }
    }

    @Override
    public List<Book> findAll(User user) {
        return bookRepository.findAll().stream().filter(book -> book.getUser() == user).toList();
    }

    private static String fetchDataFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set up the request
        connection.setRequestMethod("GET");

        // Get the response code
        int responseCode = connection.getResponseCode();

        //if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();

        //} else {
           // throw new IOException("HTTP GET request failed with response code: " + responseCode);
       // }
    }


    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> findIsbns(String title, String author) throws IOException {
        List<String> isbns = new ArrayList<>();
        HttpClient httpClient = HttpClients.createDefault();

        String apiUrl = "http://openlibrary.org/search.json?title="  + title + "&author="  + author ;
        HttpGet httpGet = new HttpGet(apiUrl);

        HttpResponse response = httpClient.execute(httpGet);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            String jsonString = fetchDataFromUrl(apiUrl);

           // System.out.println(jsonString);

            String regex = "((978|979)\\d{10})";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(jsonString);



            while (matcher.find()) {
                String isbn = matcher.group(1);
                isbns.add(isbn);
                System.out.println("ISBN found: " + isbn);
            }



            // Verwende Jsoup, um HTML-Tags zu entfernen
            /*String cleanJson = Jsoup.parse(result.toString()).text();

            JsonNode jsonNode = objectMapper.readTree(cleanJson);

            Extrahiere ISBNs aus dem JSON
            JsonNode docs = jsonNode.path("docs");
            Iterator<JsonNode> iterator = docs.elements();
            while (iterator.hasNext()) {
                JsonNode doc = iterator.next();
                JsonNode isbnNode = doc.path("isbn");
                if (isbnNode.isArray() && isbnNode.size() > 0) {
                    String isbn = isbnNode.get(0).asText();
                    isbns.add(isbn);
                }
            }*/
        }

        return isbns;
    }
}
