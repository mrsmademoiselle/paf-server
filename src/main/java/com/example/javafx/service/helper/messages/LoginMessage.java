package com.example.javafx.service.helper.messages;

import com.example.javafx.service.helper.MessageKeys;
import com.example.javafx.service.helper.TokenManager;

public class LoginMessage {
    private TokenManager tokenManager = TokenManager.getInstance();

    public LoginMessage() {
        String token = tokenManager.getToken();
    }
}
