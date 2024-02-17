import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.time.LocalDate;

public class availableBooks {

    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty genre;
    private final SimpleStringProperty image;
    private final SimpleObjectProperty<LocalDate> date;
    private final SimpleStringProperty bookTitle;
    private final SimpleStringProperty bookType;
    private final SimpleObjectProperty<LocalDate> dateType;

    public availableBooks(String title, String author, String genre, String image, Date date, String bookTitle, String bookType) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.image = new SimpleStringProperty(image);
        this.date = new SimpleObjectProperty<>(date.toLocalDate());
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.bookType = new SimpleStringProperty(bookType);
        this.dateType = new SimpleObjectProperty<>(date.toLocalDate());
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getGenre() {
        return genre.get();
    }

    public String getImage() {
        return image.get();
    }

    public LocalDate getDate() {
        return date.get();
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public String getBookType() {
        return bookType.get();
    }

    public LocalDate getDateType() {
        return dateType.get();
    }

    // Add other getter and setter methods as needed
}
