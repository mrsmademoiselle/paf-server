package com.example.javafx.service;

import com.example.javafx.model.UserDto;
import com.example.javafx.service.helper.HttpConnector;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class UserService {

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

    public boolean uploadImage(byte[] imageBytes) {
        System.out.println("array: " + imageBytes.length);

        main(java.util.Base64.getEncoder().encodeToString(imageBytes));

        return HttpConnector.post("user/image/upload", imageBytes);
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

    public boolean loginUser(String username, String password) {
        UserDto userAuthDto = new UserDto(username, password, new byte[]{});

        return HttpConnector.post("user/login", userAuthDto);
    }

    public boolean updateUserInfo(String username, String password, byte[] image) {
        UserDto userAuthDto = new UserDto(username, password, image);

        return HttpConnector.post("user/update", userAuthDto);
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

        return new UserDto(jsonObject.getString("username"), image);
    }
}