package library.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.backend.Library;

import java.io.IOException;

public class AddBookController {
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField isbnField;

    private final Library library = new Library();

    @FXML
    public void handleAddBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();

        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty() || isbn == null || isbn.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a title, author, or isbn!");
        }
        else {
            library.addBook(isbn, title, author);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Book added successfully!");

            clearFields();
        }
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleBackToDashboard(javafx.event.ActionEvent actionEvent) {
        try {
            // Load the Dashboard.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("library_dashboard.fxml"));
            Parent dashboardView = loader.load();

            // Get the current stage and set the dashboard view as the new root
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(dashboardView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}