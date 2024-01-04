package libraryProjectGroup.libraryProject.lesewunschlistbuch;

import libraryProjectGroup.libraryProject.user.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public String erstelleCoverbildLink(String isbn) {
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
    public String wandeleInTIDUm(String isbn) {
        try{
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
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }


    @Override
    public boolean istVerfuegbar(String gesuchteTID, String standort) {
        try{
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
        } catch (Exception e){
            System.out.println(e);
        }
       return false;
    }


    @Override
    public boolean findByIsbnAndUser(Book book) {
        List<Book> booksWithIsbnInRepo = bookRepository.findAll().stream()
                .filter(bookFromRepo -> (bookFromRepo.getIsbns().equals(book.getIsbns())) && (bookFromRepo.getUser() == book.getUser())).toList();
        return !booksWithIsbnInRepo.isEmpty();
    }
    @Override
    public void extractAndSaveBookData(MultipartFile file, User user) {
        List<String> listOfBookList = convertCsvToStr(file);
        System.out.println(listOfBookList);

        for(String str : listOfBookList){
            Book book = new Book();
            book.setUser(user);

            String isbn13VonCsv = "";
            String isbnMatch = "";
            Set<String> isbnListe = new HashSet<>();
            String[] eachBookInArr = str.split(",");

            //findet die ISBN von CSV Datei(Goodreads)
            List<String> isbnMatchList = Arrays.stream(eachBookInArr)
                    .filter(item -> item.matches("\\W*(978|979)\\d{10}\\W*"))
                    .toList();

            if(!isbnMatchList.isEmpty()){
                int indexOfFirst9ofFirstIsbn = isbnMatchList.get(0).indexOf('9');
                System.out.println(isbnMatchList.get(0));
                isbn13VonCsv = isbnMatch.substring(indexOfFirst9ofFirstIsbn);
                isbnListe.add(isbn13VonCsv);
                book.setCoverbild(erstelleCoverbildLink(isbn13VonCsv));
            }

            //findet die ISBNs von OpenLibrary über Author und Title
            isbnListe.addAll(findAdditionalIsbns(eachBookInArr[1],  eachBookInArr[2]));

            // erzeuge TIds
            Set<String> tidsListe = generateTids(isbnListe);

            book.setIsbns(isbnListe);
            book.setTids(tidsListe);
            book.setTitle(eachBookInArr[1]);
            book.setAuthor(eachBookInArr[2]);

            if(!findByIsbnAndUser(book) && !isbnListe.isEmpty() && !tidsListe.isEmpty()){
                bookRepository.save(book);
            }
        }
    }

    @Override
    public void saveBookToReadingWishlist(BookCreationDto dto, User user) {
        Book book = new Book();
        Set<String> isbnListe = new HashSet<>();

        isbnListe.add(dto.getIsbn());
        isbnListe.addAll(findAdditionalIsbns(dto.getTitle(), dto.getAuthor()));

        Set<String> tidsListe = generateTids(isbnListe);

        book.setUser(user);
        book.setIsbns(isbnListe);
        book.setTids(tidsListe);
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());

        if (!findByIsbnAndUser(book)) {
            bookRepository.save(book);
        }
    }

    @Override
    public List<Book> findAll(User user) {
        return bookRepository.findAll().stream().filter(book -> book.getUser() == user).toList();
    }
   @Override
    public List<String> convertCsvToStr(MultipartFile file) {
        List<String> listOfBookList = new ArrayList<>();
        try  {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            List<List<String>> data = reader.lines()
                    .skip(1)
                    .map(line -> List.of(line.split("\\s*\n\\s*")))
                    .toList();

            for(List<String> s : data){
                listOfBookList.addAll(s);
            }
            return listOfBookList;
        } catch (IOException e) {
            System.out.println("in extractAndSaveBookData");
            System.out.println(e);
           // e.printStackTrace(); // Handle the exception according to your needs
        }
        return null;
    }

    private static String fetchDataFromUrl(String urlString) {
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();
        } catch (Exception e){
            System.out.println(e);
        }
        return null;
    }


    //private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<String> findAdditionalIsbns(String title, String author) {
        try{
            List<String> isbns = new ArrayList<>();
            HttpClient httpClient = HttpClients.createDefault();

            String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
            String encodedAuthor = URLEncoder.encode(author, StandardCharsets.UTF_8.toString());

            String apiUrl = "http://openlibrary.org/search.json?title="  + encodedTitle + "&author="  + encodedAuthor ;
            HttpGet httpGet = new HttpGet(apiUrl);

            HttpResponse response = httpClient.execute(httpGet);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                String jsonString = fetchDataFromUrl(apiUrl);

                String regex = "((978|979)\\d{10})";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(jsonString);

                while (matcher.find()) {
                    String isbn = matcher.group(1);
                    isbns.add(isbn);
                    System.out.println("ISBN found: " + isbn);
                }
            }

            return isbns;
        } catch (IOException e){
            System.out.println(e);
        }
       return null;
    }

    private Set<String> generateTids(Set<String> isbnListe) {
        try{
            List<String> tidsListe = new ArrayList<>();
            for(String isbn : isbnListe){
                if(wandeleInTIDUm(isbn) != null){
                    tidsListe.add(wandeleInTIDUm(isbn));
                }
            }
            return tidsListe.stream().filter(Objects::nonNull).filter(tid -> !tid.isBlank()).collect(Collectors.toSet());
        } catch (Exception e){
            System.out.println(e);
        }
       return null;
    }

}
