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

public class AddUserController {

    @FXML
    private TextField userIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;

    private final Library library = new Library();

    @FXML
    public void handleAddUser() {
        String id = userIdField.getText();
        String userName = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (id == null || id.trim().isEmpty() || userName == null || userName.trim().isEmpty() || email == null || email.trim().isEmpty() || phone == null || phone.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a user ID, name, email, and phone number!");
        }
        else {
            library.addUser(id, userName, email, phone);

            showAlert(Alert.AlertType.INFORMATION, "Success", "User Added successfully!");

            clearFields();
        }
    }

    private void clearFields() {
        userIdField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
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
