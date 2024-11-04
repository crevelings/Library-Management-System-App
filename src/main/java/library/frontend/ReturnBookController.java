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

public class ReturnBookController {
    @FXML
    private TextField IDField;

    @FXML
    private TextField titleField;

    private final Library library = new Library();

    @FXML
    public void handleReturnBook() {
        String title = titleField.getText();
        String id = IDField.getText();

        if (title == null || title.trim().isEmpty() || id == null || id.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a title or User ID!");
        }
        else {
            library.returnBook(id, title);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Book Returned successfully!");

            clearFields();
        }
    }

    private void clearFields() {
        titleField.clear();
        IDField.clear();
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

