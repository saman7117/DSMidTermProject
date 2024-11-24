module com.example.dsmidtermproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.dsmidtermproject to javafx.fxml;
    exports com.example.dsmidtermproject;
}