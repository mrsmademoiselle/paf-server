package com.example.javafx.service;

import com.example.javafx.model.UserDto;
import com.example.javafx.service.helper.HttpConnector;
import com.example.javafx.service.helper.SceneManager;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class UserService {
    SceneManager sceneManager = SceneManager.getInstance();

    public UserDto getUserInfo() {
        String body = HttpConnector.get("user/info").getBody();

        if (new JSONObject(body).has("error")) {
            // TODO Fehlermeldung zurückgeben
            return new UserDto();
        }
        UserDto userDto = getUserDtoFromResponseBody(body);
        return userDto;
    }

    public boolean registerUser(String username, String password) {
        UserDto userAuthDto = new UserDto(username, password, new byte[]{});
        return HttpConnector.post("user/register", userAuthDto);
    }

    public void uploadImage(byte[] imageBytes) {
        System.out.println("array: " + imageBytes.length);
        
        main(java.util.Base64.getEncoder().encodeToString(imageBytes));

        boolean successfullyUploaded = HttpConnector.post("user/image/upload", imageBytes);
        if (successfullyUploaded) {
            sceneManager.loadAccountData();
            // sceneController.loadLobby();
        }
    }

    public void main(String s) {
        try {
            FileWriter writer = new FileWriter("MyFile.txt", true);
            writer.write(s);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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

        boolean imageIsThere = jsonObject.has("profilbild") && !jsonObject.get("profilbild").equals(JSONObject.NULL);
        byte[] image = imageIsThere
                ? java.util.Base64.getDecoder().decode(jsonObject.getString("profilbild"))
                : new byte[]{};

        UserDto userDto = new UserDto(jsonObject.getString("username"), image);
        return userDto;
    }
}