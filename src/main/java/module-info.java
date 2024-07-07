module com.solitarios {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.solitarios to javafx.fxml;
    exports com.solitarios;
    opens com.solitarios.controller to javafx.fxml;
    exports com.solitarios.controller;
}