package library.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import javafx.stage.Stage;
import library.backend.Book;
import library.backend.Library;
import library.backend.User;

public class OverdueBooksController {
    @FXML
    private TableColumn<Book, String> userColumn;

    @FXML
    private TableColumn<Book, String> bookColumn;

    @FXML
    private TableColumn<Book, LocalDate> dueDateColumn;

    public Library library = new Library();

    public void initialize() {
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        bookColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        library.listOverdueBooks();
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
