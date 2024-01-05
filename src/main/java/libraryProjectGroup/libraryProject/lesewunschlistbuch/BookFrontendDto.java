package libraryProjectGroup.libraryProject.lesewunschlistbuch;

public class BookFrontendDto {

    private String coverbild;
    private String title;
    private String author;

    private int bookId;

    private String standortLink;

    public String getStandortLink() {
        return standortLink;
    }

    public void setStandortLink(String standortLink) {
        this.standortLink = standortLink;
    }


    public String getCoverbild() {
        return coverbild;
    }

    public void setCoverbild(String coverbild) {
        this.coverbild = coverbild;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
}
