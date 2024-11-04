module com.example.librarymanagementsystemapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;

    opens library.backend to javafx.base;

    opens library.frontend to javafx.fxml;
    exports library.frontend;
}