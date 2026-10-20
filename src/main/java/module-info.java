module com.mycompany.weinhaendler {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.weinhaendler to javafx.fxml;
    exports com.mycompany.weinhaendler;
}
