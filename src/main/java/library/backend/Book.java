package library.backend;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Book {
    private final String title;
    private final String author;
    private final String ISBNNumber;
    private boolean isAvailable;
    private final IntegerProperty totalCopies = new SimpleIntegerProperty(this, "totalCopies", 0);

    public Book(String title, String author, String ISBNNumber, int totalCopies) {
        this.title = title;
        this.author = author;
        this.ISBNNumber = ISBNNumber;
        this.isAvailable = true;
        this.totalCopies.set(totalCopies);
    }

    public int getTotalCopies() {
        return totalCopies.get();
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies.set(totalCopies);
    }

    public IntegerProperty totalCopiesProperty() {
        return totalCopies;
    }

    public String getIsbn() {
        return ISBNNumber;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    @Override
    public String toString() {
        return "Book{" + "title='" + title + '\'' + ", author='" + author + '\'' +
                ", isAvailable=" + isAvailable + '}';
    }
}

