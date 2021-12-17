package com.example.javafx.service;

import com.example.javafx.model.UserDto;
import com.example.javafx.service.helper.HttpConnector;
import com.example.javafx.service.helper.SceneManager;
import org.json.JSONObject;

public class UserService {
    SceneManager sceneManager = SceneManager.getInstance();

    public UserDto getUserInfo() {
        String body = HttpConnector.get("user/info").getBody();
        UserDto userDto = getUserDtoFromResponseBody(body);
        return userDto;
    }

    public boolean registerUser(String username, String password) {
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

    public UserDto removeImage() {
        String body = HttpConnector.get("user/image/remove").getBody();
        UserDto userDto = getUserDtoFromResponseBody(body);

        return userDto;
    }

    private UserDto getUserDtoFromResponseBody(String body) {
        JSONObject jsonObject = new JSONObject(body);

        byte[] image = jsonObject.get("profileImage").equals(JSONObject.NULL)
                ? new byte[]{}
                : java.util.Base64.getDecoder().decode(jsonObject.getString("profileImage"));

        UserDto userDto = new UserDto(jsonObject.getString("username"), image);
        return userDto;
    }
}