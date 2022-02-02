package com.example.javafx.service;

import com.example.javafx.model.UserDto;
import com.example.javafx.service.helper.HttpConnector;
import org.json.JSONObject;

public class UserService {

    public UserDto getUserInfo() {
        String body = HttpConnector.get("user/info").getBody();

        if (new JSONObject(body).has("error")) {
            return new UserDto();
        }
        return getUserDtoFromResponseBody(body);
    }

    public boolean registerUser(String username, String password) {
        UserDto userAuthDto = new UserDto(username, password, new byte[]{});
        return HttpConnector.post("user/register", userAuthDto);
    }

    public boolean uploadImage(byte[] imageBytes) {
        return HttpConnector.post("user/image/upload", imageBytes);
    }

    public boolean loginUser(String username, String password) {
        UserDto userAuthDto = new UserDto(username, password, new byte[]{});

        return HttpConnector.post("user/login", userAuthDto);
    }

    public boolean updateUser(String username, String password, byte[] image) {
        UserDto userAuthDto = new UserDto(username, password, image);

        return HttpConnector.post("user/update", userAuthDto);
    }

    public UserDto removeImage() {
        String body = HttpConnector.get("user/image/remove").getBody();

        return getUserDtoFromResponseBody(body);
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