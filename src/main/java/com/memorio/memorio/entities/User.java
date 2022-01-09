package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.*;

/**
 * Das User-Objekt repräsentiert einen User für das Spiel.
 */
@ToString
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

}