package com.example.javafx;

import com.example.javafx.controller.SceneController;
import com.example.javafx.model.UserAuthDto;

public class UserService {
    SceneController sceneController = SceneController.getInstance();

    public boolean registerUserData(String username, String password) {
        UserAuthDto userAuthDto = new UserAuthDto(username, password);
        return HttpConnector.post("user/register", userAuthDto);
    }

    public void uploadImage(byte[] imageBytes) {
        boolean successfullyUploaded = HttpConnector.post("user/image/upload", imageBytes);
        if (successfullyUploaded) {
            sceneController.loadAccountData();
            // sceneController.loadLobby();
        }
    }

    public void loginUser(String username, String password) {
        UserAuthDto userAuthDto = new UserAuthDto(username, password);

        boolean isOk = HttpConnector.post("user/login", userAuthDto);
        if (isOk) {
            sceneController.loadAccountData();
        }
    }

    public boolean updateUserInfo(String username, String password) {
        if (username.matches("[\\w|\\d]*") && username.isBlank() && password.isBlank()) {
            UserAuthDto userAuthDto = new UserAuthDto(username, password);

            return HttpConnector.post("user/update", userAuthDto);
        }
        return false;
    }

    public void redirectToAccount() {
        sceneController.loadAccountData();
    }

    public void redirectToLogin() {
        sceneController.loadLogin();
    }

    public void redirectToRegister() {
        sceneController.loadRegistration();
    }
}