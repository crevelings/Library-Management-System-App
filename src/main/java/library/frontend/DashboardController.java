package library.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import library.backend.DBConnection;
import library.backend.Library;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardController {
    @FXML
    private Label bookCountLabel;

    @FXML
    private Label userCountLabel;

    @FXML
    private Label librarianCountLabel;

    @FXML
    private Label loanCountLabel;

    @FXML
    private Label overdueBookCountLabel;

    @FXML
    private Label librarianNameLabel;

    @FXML
    private Label dateLabel;

    private final Library library = new Library();

    @FXML
    public void initialize() {
        updateBookCount();
        updateUserCount();
        updateLibrarianCount();
        updateLoanCount();
        updateOverdueBookCount();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy"); // e.g., "October 28, 2024"
        String formattedDate = today.format(formatter);

        // Set the formatted date to the label
        dateLabel.setText(formattedDate);
    }

    public void setLibrarianName(String librarianName) {
        librarianNameLabel.setText("Welcome, " + librarianName + "!");
    }

    private void updateBookCount() {
        int totalBooks = library.getBooks().size(); // Assuming getBooks() returns a List<Book>
        bookCountLabel.setText(String.valueOf(totalBooks)); // Set the count in the label
    }

    private void updateUserCount() {
        String query = "SELECT COUNT(*) FROM users";

        try (Connection connection = DBConnection.getLibraryConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int userCount = resultSet.getInt(1);
                userCountLabel.setText(String.valueOf(userCount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            userCountLabel.setText("Error");
        }
    }

    private void updateLibrarianCount() {
        String query = "SELECT COUNT(*) FROM Librarians";

        try (Connection connection = DBConnection.getLibraryConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int librarianCount = resultSet.getInt(1);
                librarianCountLabel.setText(String.valueOf(librarianCount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            librarianCountLabel.setText("Error");
        }
    }

    private void updateLoanCount() {
        String query = "SELECT COUNT(*) FROM Loans";

        try (Connection connection = DBConnection.getLibraryConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int loanCount = resultSet.getInt(1);
                loanCountLabel.setText(String.valueOf(loanCount));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            loanCountLabel.setText("Error");
        }
    }

    private void updateOverdueBookCount() {
        // SQL query to count overdue books in the 'loans' table
        String query = "SELECT COUNT(*) FROM loans WHERE due_date < CURDATE() AND return_date IS NULL";

        try (Connection connection = DBConnection.getLibraryConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                int overdueBookCount = resultSet.getInt(1); // Get the overdue book count
                overdueBookCountLabel.setText(String.valueOf(overdueBookCount)); // Set the count in the label
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception by printing stack trace (or logging it)
            overdueBookCountLabel.setText("Error"); // Display error if something goes wrong
        }
    }

    @FXML
    private void handleListBooks(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("list_books.fxml"));
            Parent listBooksView = loader.load();

            ListBooksController controller = loader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(listBooksView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddBook(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_book.fxml"));
            Parent addBookView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(addBookView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleIssueBook(ActionEvent event) {
        try {
            // Load the Add Book view from FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("issue_book.fxml"));
            Parent issueBookView = loader.load();

            // Replace the current view with the Add Book view
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(issueBookView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReturnBook(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("return_book.fxml"));
            Parent returnBookView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(returnBookView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCheckLoans(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("check_loans.fxml"));
            Parent checkLoansView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(checkLoansView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOverdueBooks(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("overdue_books.fxml"));
            Parent overdueBooksView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(overdueBooksView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser(ActionEvent event) {
        try {
            // Load the Add Book view from FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_user.fxml"));
            Parent addUserView = loader.load();

            // Replace the current view with the Add Book view
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(addUserView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddLibrarian(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_librarian.fxml"));
            Parent addLibrarianView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(addLibrarianView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.out.println("Exiting application...");
        System.exit(0);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

