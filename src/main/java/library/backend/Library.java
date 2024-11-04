package library.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.sql.*;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Library {
    private final List<User> users;

    public Library() {
        this.users = new ArrayList<>();
    }
    // Method to log in the librarian
    public boolean librarianLogin(String username, String password) {
        String sql = "SELECT password FROM Librarians WHERE username = ?";

        try (Connection conn = DBConnection.getLibraryConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                return verifyPassword(password, storedHash);  // Password comparison
            } else {
                System.out.println("Username not found.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hashing method for storing/validating passwords
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Method to verify password
    public boolean verifyPassword(String password, String storedHash) {
        String passwordHash = hashPassword(password);
        return passwordHash.equals(storedHash);
    }

    public void addLibrarian(String username, String password) {
        String sql = "INSERT INTO Librarians (username, password) VALUES (?, ?)";

        try (Connection conn = DBConnection.getLibraryConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String hashedPassword = hashPassword(password);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            System.out.println("Librarian added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getLibrarianName(String username) {
        String query = "SELECT username FROM Librarians WHERE username = ?";
        try (Connection connection = DBConnection.getLibraryConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void addBook(String isbn, String title, String author) {
        String checkBookSQL = "SELECT COUNT(*) AS count FROM Books WHERE isbn = ?";
        String insertBookSQL = "INSERT INTO Books (isbn, title, author) VALUES (?, ?, ?)";
        String checkCopiesSQL = "SELECT num_copies FROM Copies WHERE isbn = ?";
        String insertCopySQL = "INSERT INTO Copies (num_copies, isbn) VALUES (?, ?)";
        String updateCopySQL = "UPDATE Copies SET num_copies = ? WHERE isbn = ?";

        try (Connection conn = DBConnection.getLibraryConnection();
             PreparedStatement checkBookStmt = conn.prepareStatement(checkBookSQL);
             PreparedStatement insertBookStmt = conn.prepareStatement(insertBookSQL);
             PreparedStatement checkCopiesStmt = conn.prepareStatement(checkCopiesSQL);
             PreparedStatement insertCopyStmt = conn.prepareStatement(insertCopySQL);
             PreparedStatement updateCopyStmt = conn.prepareStatement(updateCopySQL)) {

            // Check if the book already exists in the Books table
            checkBookStmt.setString(1, isbn);
            ResultSet bookExists = checkBookStmt.executeQuery();

            if (bookExists.next() && bookExists.getInt("count") == 0) {
                // Add the new book to the Books table
                insertBookStmt.setString(1, isbn);
                insertBookStmt.setString(2, title);
                insertBookStmt.setString(3, author);
                insertBookStmt.executeUpdate();
                System.out.println("Book added to Books table.");
            }

            // Check if the book already has copies in the Copies table
            checkCopiesStmt.setString(1, isbn);
            ResultSet copiesResult = checkCopiesStmt.executeQuery();

            if (copiesResult.next()) {
                // If the book has copies, increment the num_copies
                int currentCopies = copiesResult.getInt("num_copies");
                int newCopyCount = currentCopies + 1;

                updateCopyStmt.setInt(1, newCopyCount);
                updateCopyStmt.setString(2, isbn);
                updateCopyStmt.executeUpdate();

                System.out.println("Updated copy count to " + newCopyCount + " for isbn: " + isbn);
            } else {
                // If no copies exist, add the first copy
                insertCopyStmt.setInt(1, 1); // First copy
                insertCopyStmt.setString(2, isbn);
                insertCopyStmt.executeUpdate();

                System.out.println("First copy added for book: " + title);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String userId, String name, String email, String phone) {
        executeUpdate(userId, name, email, phone);
        System.out.println("User added successfully!");
    }

    public void registerNewUserFromInput(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user phone number: ");
        String phone = scanner.nextLine();

        String email = getValidEmail(scanner);
        if (findUserById(userId) != null) {
            System.out.println("User ID is already taken. Try again.");
            return;
        }

        addUser(userId, name, email, phone);
        users.add(new User(userId, name, phone, email));
        System.out.println("User registered successfully: " + name);
    }

    private String getValidEmail(Scanner scanner) {
        String email;
        do {
            System.out.print("Enter user email (must end with .com): ");
            email = scanner.nextLine();
        } while (!isValidEmail(email));
        return email;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$");
    }

    public void issueBook(String userId, String title) {
        String checkBookSQL = "SELECT isbn FROM Books WHERE title = ?";
        String checkCopiesSQL = "SELECT num_copies FROM Copies WHERE isbn = ?";
        String decrementCopySQL = "UPDATE Copies SET num_copies = num_copies - 1 WHERE isbn = ? AND num_copies > 0";
        String loanInsertSQL = "INSERT INTO Loans (user_id, isbn, borrow_date, due_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getLibraryConnection()) {
            // Get the ISBN for the book with the given title
            String isbn = null;
            try (PreparedStatement checkBookStmt = conn.prepareStatement(checkBookSQL)) {
                checkBookStmt.setString(1, title);
                ResultSet rs = checkBookStmt.executeQuery();
                if (rs.next()) {
                    isbn = rs.getString("isbn");
                } else {
                    System.out.println("Book with the title '" + title + "' not found.");
                    return; // Exit if the book is not found
                }
            }

            try (PreparedStatement checkCopiesStmt = conn.prepareStatement(checkCopiesSQL)) {
                checkCopiesStmt.setString(1, isbn);
                ResultSet rs = checkCopiesStmt.executeQuery();

                if (rs.next() && rs.getInt("num_copies") > 0) {
                    // Decrement the copy number
                    try (PreparedStatement decrementStmt = conn.prepareStatement(decrementCopySQL)) {
                        decrementStmt.setString(1, isbn);
                        decrementStmt.executeUpdate();
                    }

                    // Issue the book
                    try (PreparedStatement loanStmt = conn.prepareStatement(loanInsertSQL)) {
                        loanStmt.setString(1, userId);
                        loanStmt.setString(2, isbn);
                        loanStmt.setDate(3, java.sql.Date.valueOf(LocalDate.now())); // Loan date
                        loanStmt.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusWeeks(2))); // Due date
                        loanStmt.executeUpdate();
                    }

                    System.out.println("Book issued successfully!");
                } else {
                    System.out.println("No available copies to issue.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook(String userId, String title) {
        String checkBookSQL = "SELECT isbn FROM Books WHERE title = ?";
        String checkLoanSQL = "SELECT * FROM Loans WHERE user_id = ? AND isbn = ?";
        String incrementCopySQL = "UPDATE Copies SET num_copies = num_copies + 1 WHERE isbn = ?";
        String deleteLoanSQL = "DELETE FROM Loans WHERE user_id = ? AND isbn = ?";

        try (Connection conn = DBConnection.getLibraryConnection()) {
            // Get the ISBN for the book with the given title
            String isbn;
            try (PreparedStatement checkBookStmt = conn.prepareStatement(checkBookSQL)) {
                checkBookStmt.setString(1, title);
                ResultSet rs = checkBookStmt.executeQuery();
                if (rs.next()) {
                    isbn = rs.getString("isbn");
                } else {
                    System.out.println("Book with the title '" + title + "' not found.");
                    return; // Exit if the book is not found
                }
            }

            // Check if the loan exists
            try (PreparedStatement checkLoanStmt = conn.prepareStatement(checkLoanSQL)) {
                checkLoanStmt.setString(1, userId);
                checkLoanStmt.setString(2, isbn);
                ResultSet rs = checkLoanStmt.executeQuery();

                if (rs.next()) {
                    // Increment the copy count
                    try (PreparedStatement incrementStmt = conn.prepareStatement(incrementCopySQL)) {
                        incrementStmt.setString(1, isbn);
                        incrementStmt.executeUpdate();
                    }

                    // Delete the loan record
                    try (PreparedStatement deleteLoanStmt = conn.prepareStatement(deleteLoanSQL)) {
                        deleteLoanStmt.setString(1, userId);
                        deleteLoanStmt.setString(2, isbn);
                        deleteLoanStmt.executeUpdate();
                    }

                    System.out.println("Book returned successfully!");
                } else {
                    System.out.println("No loan record found for this user and book.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Loan> checkActiveLoans() {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT loan_id, ISBN, user_id, borrow_date, due_date FROM Loans WHERE return_date IS NULL";

        try (Connection conn = DBConnection.getLibraryConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            // Check if there are no loans
            if (!rs.isBeforeFirst()) {
                System.out.println("No loans.");
            } else {
                while (rs.next()) {
                    int loanId = rs.getInt("loan_id");
                    String isbn = rs.getString("ISBN");
                    String userId = rs.getString("user_id");
                    LocalDate borrowDate = rs.getDate("borrow_date").toLocalDate();
                    LocalDate dueDate = rs.getDate("due_date").toLocalDate();

                    loans.add(new Loan(loanId, isbn, userId, borrowDate, dueDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }


    public void listOverdueBooks() {
        LocalDate today = LocalDate.now();
        boolean overdueFound = false;

        System.out.println("Listing overdue books:");
        for (User user : users) {
            Map<Book, LocalDate> borrowedBooks = user.getBorrowedBooksWithDueDates();
            if (borrowedBooks.isEmpty()) {
                System.out.println(user.getName() + " has no books issued.");
                continue;
            }

            for (Map.Entry<Book, LocalDate> entry : borrowedBooks.entrySet()) {
                if (entry.getValue().isBefore(today)) {
                    System.out.println("Book: " + entry.getKey().getTitle() + ", User: " + user.getName() + ", Due Date: " + entry.getValue());
                    overdueFound = true;
                }
            }
        }

        if (!overdueFound) {
            System.out.println("No overdue books in the library.");
        }
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();

        String query = "SELECT b.title, b.author, b.isbn, COALESCE(SUM(c.num_copies), 0) AS total_copies " +
                "FROM Books b " +
                "LEFT JOIN Copies c ON b.isbn = c.isbn " +
                "GROUP BY b.title, b.author, b.isbn";

        try (Connection conn = DBConnection.getLibraryConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("total_copies")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    private void executeUpdate(String... params) {
        try (Connection conn = DBConnection.getLibraryConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users (user_id, name, email, phone) VALUES (?, ?, ?, ?)")) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected User findUserById(String userId) {
        return users.stream().filter(user -> user.getUserId().equals(userId)).findFirst().orElse(null);
    }
}