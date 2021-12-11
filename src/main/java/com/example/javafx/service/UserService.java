package com.example.javafx.service;

import com.example.javafx.model.UserDto;
import com.example.javafx.service.helper.HttpConnector;
import com.example.javafx.service.helper.SceneManager;
import org.json.JSONObject;

public class UserService {
    SceneManager sceneManager = SceneManager.getInstance();

    public UserDto getUserInfo() {
        JSONObject jsonObject = new JSONObject(HttpConnector.get("user/username").getBody());
        // UserDto userDto = new UserDto(jsonObject.getString("username"), jsonObject.getString("image").getBytes(StandardCharsets.UTF_8));
        return new UserDto("nevergonnagiveyouup", "", new byte[]{});
    }

    public boolean registerUserData(String username, String password) {
        UserDto userAuthDto = new UserDto(username, password, new byte[]{});
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
        UserDto userAuthDto = new UserDto(username, password, new byte[]{});

        boolean isOk = HttpConnector.post("user/login", userAuthDto);
        if (isOk) {
            sceneManager.loadAccountData();
        }
    }

    public boolean updateUserInfo(String username, String password, byte[] image) {
        UserDto userAuthDto = new UserDto(username, password, image);

        return HttpConnector.post("user/update", userAuthDto);
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