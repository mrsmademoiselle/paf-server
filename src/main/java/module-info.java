module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.prefs;
    requires json;
    requires coresearch.cvurl.io;

    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
    exports com.example.javafx.controller;
    exports com.example.javafx.model;
    opens com.example.javafx.controller to javafx.fxml;
}