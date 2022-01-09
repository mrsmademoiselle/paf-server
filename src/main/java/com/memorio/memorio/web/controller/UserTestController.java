package com.memorio.memorio.web.controller;

import com.memorio.memorio.entities.GameHistory;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.services.GameHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;


@RestController
/* Transactional zeigt an, dass jede aufgerufene Methode eine abgeschlossene Transaktion abbildet. In einer Transaktion
 übernimmt JPA bestimmte Operationen automatisch, z.B. das committen von Changes (Persistence Context) */
@Transactional
@RequestMapping("/user")
public class UserTestController {
    private final UserRepository userRepository;
    private final GameHistoryService gameHistoryService;
    private final Logger logger = LoggerFactory.getLogger(UserTestController.class);

    @Autowired
    public UserTestController(UserRepository userRepository, GameHistoryService gameHistoryService) {
        this.userRepository = userRepository;
        this.gameHistoryService = gameHistoryService;

    }

    @GetMapping("/all")
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    /**
     * Dieser Endpunkt ist öffentlich (ohne JWT) zugänglich und nur für Testzwecke da.
     * Er kann nach dem Spielhistorie-Sprint wieder ausgebaut werden.
     * <p>
     * Da wir uns in MemorioApplication.java Testdaten anlegen, können hiermit diese Testdaten
     * vom Server abgerufen werden, ohne dass das Spiel erst komplett durchgespielt werden muss. ;-)
     * <p>
     * Gibt für User, die noch keine Spiele gespielt haben, random Werte zurück, da es beim Einloggen
     * mit den Testdaten aufgrund der Passworthashes noch Probleme gibt.
     * Somit können wir uns jetzt auf den Clients neue Spieler erstellen, und für diese werden direkt
     * random Werte zurückgegeben.
     */
    @GetMapping("/history/test")
    public ResponseEntity<GameHistory> getUserHistoryTest(@RequestHeader(name = "Username") String username) {
        GameHistory gameHistory = gameHistoryService.getGameHistoryForUser(username);
        logger.info("Returning game history for user " + username + ": " + gameHistory);

        return ResponseEntity.ok(gameHistory);
    }

}