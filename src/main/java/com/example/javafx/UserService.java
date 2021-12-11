package com.example.javafx;

import com.example.javafx.model.UserAuthDto;

public class UserService {
    public boolean registerUser() {
        return true;
    }

    public boolean loginUser() {
        return false;
    }

    public boolean updateUserInfo(String username, String password) {
        if (username.matches("[\\w|\\d]*") && username.isBlank() && password.isBlank()) {
            UserAuthDto userAuthDto = new UserAuthDto(username, password);

            return HttpConnector.post("user/update", userAuthDto);
        }
        return false;
    }
}