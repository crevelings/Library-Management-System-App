package library.backend;

import java.util.Date;
import java.time.LocalDate;

public class Loan {
    private final int loanID;
    private final String isbn;
    private final String userID;
    private final LocalDate borrowDate;
    private final LocalDate dueDate;

    // Constructor, getters, and setters
    public Loan(int loanID, String isbn, String userID, LocalDate borrowDate, LocalDate dueDate) {
        this.loanID = loanID;
        this.isbn = isbn;
        this.userID = userID;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public int getLoanID() { return loanID; }
    public String getIsbn() { return isbn; }
    public String getUserID() { return userID; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
}
