package libraryProjectGroup.libraryProject.lesewunschlistbuch;

import libraryProjectGroup.libraryProject.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public boolean findByIsbnAndUser(Book book) {
        List<Book> booksWithIsbnInRepo = bookRepository.findAll().stream()
                .filter(bookFromRepo -> (bookFromRepo.getIsbn().equals(book.getIsbn())) && (bookFromRepo.getUser() == book.getUser())).toList();
        return !booksWithIsbnInRepo.isEmpty();
    }
    @Override
    public void extractAndSaveBookData(String[] books, User user){
        for(String str : books){
            Book book = new Book();
            book.setUser(user);

            String isbn13 = "";
            String isbnMatch = "";
            String[] eachBookInArr = str.split(",");

            book.setTitle(eachBookInArr[1]);
            book.setAuthor(eachBookInArr[2]);

            List<String> isbnMatchList = Arrays.stream(eachBookInArr)
                    .filter(item -> item.matches("\\W*(978|979)\\d{10}\\W*"))
                    .toList();
            if(!isbnMatchList.isEmpty()){
                isbnMatch = isbnMatchList.get(0);
                int indexOfFirst9 = isbnMatch.indexOf('9');
                isbn13 = isbnMatch.substring(indexOfFirst9, indexOfFirst9 + 13);
                book.setIsbn(isbn13);
            } else {
                book.setIsbn("9780000000000"); // oder andere Lösung?
            }
            if(!findByIsbnAndUser(book)){
                bookRepository.save(book);
            }
        }
    }

    @Override
    public void saveBookToReadingWishlist(BookCreationDto bookOfUser, User user) {
        Book book = new Book();
        book.setUser(user);
        book.setIsbn(bookOfUser.getIsbn());
        book.setTitle(bookOfUser.getTitle());
        book.setAuthor(bookOfUser.getAuthor());

        if(!findByIsbnAndUser(book)){
            bookRepository.save(book);
        }
    }
}
