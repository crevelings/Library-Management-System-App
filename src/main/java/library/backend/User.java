package library.backend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

public class User {
    private final String userId;
    private final String name;
    private final String phoneNumber;
    private final String email;
    protected final List<Book> borrowedBooks;
    private final Map<Book, LocalDate> borrowedBooksWithDueDates;

    public User(String userId, String name, String phoneNumber, String email) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
        this.borrowedBooksWithDueDates = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public Map<Book, LocalDate> getBorrowedBooksWithDueDates() {
        return borrowedBooksWithDueDates;
    }
}

