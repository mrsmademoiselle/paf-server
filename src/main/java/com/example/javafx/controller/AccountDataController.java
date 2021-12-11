package com.example.javafx.controller;

import com.example.javafx.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;

public class AccountDataController {

    @FXML
    Circle profilePic;

    @FXML
    TextField usernameTextfield;

    @FXML
    PasswordField passwordTextfield;

    @FXML
    BannerController bannerController;

    UserService userService = new UserService();

    @FXML
    public void initialize() {
        setProfilePic();

//        JSONObject jsonObject = new JSONObject(HttpConnector.get("user/username").getBody());
//        usernameTextfield.setText(jsonObject.getString("username"));
        usernameTextfield.setText("never_gonna_give_you_up69");
    }

    public void setProfilePic() {
        Image pic = getPic("painting2.png");
        if (pic != null) {
            profilePic.setFill(new ImagePattern(pic));
            profilePic.setRadius(100);
        }
    }

    private Image getPic(String fileName) {
        File folder = new File("src/main/resources/com/example/javafx/pics");
        if (folder.exists()) {
            File imageFile = new File(folder, fileName);

            return new Image(imageFile.toURI().toString());
        }
        return null;
    }

    public void updateUserInfo(MouseEvent mouseEvent) {
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();

        boolean successful = userService.updateUserInfo(username, password);

        String bannerText = successful ? "Die Userinformationen wurden erfolgreich bearbeitet." : "Die Userinformationen konnten nicht bearbeitet werden.";
        bannerController.setText(bannerText, successful);
    }
}