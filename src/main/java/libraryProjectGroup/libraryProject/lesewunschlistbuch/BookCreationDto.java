package libraryProjectGroup.libraryProject.lesewunschlistbuch;

public class BookCreationDto {
    private final String isbn;
    private final String title;
    private final String author;

    public BookCreationDto(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}
