package libraryProjectGroup.libraryProject.lesewunschlistbuch;

import libraryProjectGroup.libraryProject.user.User;

public interface BookService {
    boolean findByIsbnAndUser(Book book);
    void extractAndSaveBookData(String[] books, User user);
    void saveBookToReadingWishlist(BookCreationDto book, User user);
}
