module com.example.dsmidtermproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;


    opens com.example.dsmidtermproject to javafx.fxml;
    exports com.example.dsmidtermproject;
}