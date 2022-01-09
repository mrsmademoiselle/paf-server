package com.memorio.memorio.web.controller;

import com.memorio.memorio.entities.User;
import com.memorio.memorio.services.GameHistoryService;
import com.memorio.memorio.services.UserAuthService;
import com.memorio.memorio.web.dto.GameHistoryDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
/* Transactional zeigt an, dass jede aufgerufene Methode eine abgeschlossene Transaktion abbildet. In einer Transaktion
 übernimmt JPA bestimmte Operationen automatisch, z.B. das committen von Changes (Persistence Context) */
@Transactional
@RequestMapping("/user")
public class UserHistoryController {
    private final GameHistoryService gameHistoryService;
    private final UserAuthService userAuthService;
    private final Logger logger = LoggerFactory.getLogger(UserTestController.class);

    @Autowired
    public UserHistoryController(GameHistoryService gameHistoryService, UserAuthService userAuthService) {
        this.gameHistoryService = gameHistoryService;
        this.userAuthService = userAuthService;
    }

    @GetMapping("/history")
    public ResponseEntity<GameHistoryDto> getUserHistory(@RequestHeader(name = "Authorization") String jwtToken) {
        User user = userAuthService.getUserFromJwt(jwtToken);

        logger.info("Schicke GameHistoryDto an Client " + user.getUsername() + " zurück...");
        return ResponseEntity.ok(gameHistoryService.getGameHistoryForUser(user.getUsername()));
    }
}