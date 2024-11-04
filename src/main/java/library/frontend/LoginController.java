package library.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import library.backend.Library;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    // Library system backend
    private final Library library = new Library();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        boolean loginSuccess = library.librarianLogin(username, password);

        if (loginSuccess) {
            String librarianName = library.getLibrarianName(username);
            errorLabel.setText("Login successful!");
            loadDashboard(librarianName);
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    public void initialize() {
        usernameField.setOnAction(event -> passwordField.requestFocus());

        passwordField.setOnAction(event -> handleLogin());
    }

    private void loadDashboard(String librarianName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("library_dashboard.fxml"));
            Parent dashboardRoot = loader.load();

            DashboardController dashboardController = loader.getController();
            dashboardController.setLibrarianName(librarianName);

            Stage stage = (Stage) usernameField.getScene().getWindow();

            stage.setScene(new Scene(dashboardRoot, 1000, 750));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}