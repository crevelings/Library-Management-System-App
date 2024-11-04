package library.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import library.backend.Library;

import java.io.IOException;

public class AddLibrarianController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private final Library library = new Library();
    @FXML
    private void handleAddLibrarian() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a username or password.");
            return;
        }

        try {
            // Call the addLibrarian method to add the librarian
            library.addLibrarian(username, password);
            showAlert(Alert.AlertType.INFORMATION, "Success!", "Librarian added successfully.");

            // Clear input fields after successful addition
            usernameField.clear();
            passwordField.clear();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add librarian. Please try again.");
        }
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