package com.example.javafx.service;

import com.example.javafx.model.UserAuthDto;
import com.example.javafx.service.helper.HttpConnector;
import com.example.javafx.service.helper.SceneManager;

public class UserService {
    SceneManager sceneManager = SceneManager.getInstance();

    public boolean registerUserData(String username, String password) {
        UserAuthDto userAuthDto = new UserAuthDto(username, password);
        return HttpConnector.post("user/register", userAuthDto);
    }

    public void uploadImage(byte[] imageBytes) {
        boolean successfullyUploaded = HttpConnector.post("user/image/upload", imageBytes);
        if (successfullyUploaded) {
            sceneManager.loadAccountData();
            // sceneController.loadLobby();
        }
    }

    public void loginUser(String username, String password) {
        UserAuthDto userAuthDto = new UserAuthDto(username, password);

        boolean isOk = HttpConnector.post("user/login", userAuthDto);
        if (isOk) {
            sceneManager.loadAccountData();
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
        sceneManager.loadAccountData();
    }

    public void redirectToLogin() {
        sceneManager.loadLogin();
    }

    public void redirectToRegister() {
        sceneManager.loadRegistration();
    }
}