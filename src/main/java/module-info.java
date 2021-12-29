module com.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.prefs;
    requires json;
    requires coresearch.cvurl.io;
    requires org.java_websocket;

    opens com.example.javafx to javafx.fxml;
    exports com.example.javafx;
    exports com.example.javafx.controller;
    exports com.example.javafx.model;
    opens com.example.javafx.controller to javafx.fxml;
    exports com.example.javafx.service;
    opens com.example.javafx.service to javafx.fxml;
    exports com.example.javafx.service.helper;
    opens com.example.javafx.service.helper to javafx.fxml;
}