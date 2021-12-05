package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

/**
 * Das User-Objekt repräsentiert einen User für das Spiel.
 */
@Getter
@Setter
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String username;
    // TODO: Adjust datatype for password
    @Column
    private String password;
    @OneToOne
    private UserProfil userProfil;

    // Lob = "Large object" für DB
    @Lob
    @Column
    @Nullable
    private byte[] image;

    @Deprecated
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Für Testzwecke, ob Post Request mit Bild über Postman richtig ankommt
     * und persistiert wird
     */
    public void saveUserProfilePicToServer() {
        if (this.image != null) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(this.image);
                BufferedImage bImage2 = ImageIO.read(bis);
                ImageIO.write(bImage2, "jpg", new File("output.jpg"));
            } catch (Exception e) {
                System.out.println("Das hat wohl nicht geklappt");
            }
        }
    }

}