package libraryProjectGroup.libraryProject.lesewunschlistbuch;

public class BookCreationDto {
    private final String isbn;
    private final String title;
    private final String author;
//    private final String coverbild;

    public BookCreationDto(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
//        this.coverbild = "test";
    }

//    public String getCoverbild() {
//        return coverbild;
//    }

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
