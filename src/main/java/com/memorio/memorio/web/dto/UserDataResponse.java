package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserDataResponse {
    private String username;
    private byte[] profileImage;
    // das String Bild ist für den JavaFX-Client notwendig, weil wir sonst Probleme beim Parsen mit Jackson bekommen
    private String profilbild;

    public UserDataResponse(String username, String profilbild, byte[] profileImage) {
        this.username = username;
        this.profilbild = profilbild;
        this.profileImage = profileImage;
    }

    public UserDataResponse(String username, byte[] profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }
}