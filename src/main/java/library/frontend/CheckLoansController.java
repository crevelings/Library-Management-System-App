package library.frontend;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import library.backend.Library;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import library.backend.Loan;
import java.time.LocalDate;
import java.io.IOException;
import java.util.List;

public class CheckLoansController {

    @FXML
    private TableView<Loan> bookLoansTable;

    @FXML
    private TableColumn<Loan, Integer> loanIDColumn;

    @FXML
    private TableColumn<Loan, String> isbnColumn;

    @FXML
    private TableColumn<Loan, String> userIDColumn;

    @FXML
    private TableColumn<Loan, LocalDate> borrowDateColumn;

    @FXML
    private TableColumn<Loan, LocalDate> dueDateColumn;

    private final Library library = new Library();

    @FXML
    public void initialize() {
        loanIDColumn.setCellValueFactory(new PropertyValueFactory<>("loanID"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        List<Loan> loanList = library.checkActiveLoans();
        ObservableList<Loan> loanData = FXCollections.observableArrayList(loanList);
        bookLoansTable.setItems(loanData);
    }

    @FXML
    public void handleBackToDashboard(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("library_dashboard.fxml"));
            Parent dashboardView = loader.load();
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(dashboardView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
