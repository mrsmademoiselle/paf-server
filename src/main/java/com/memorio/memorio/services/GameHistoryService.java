package com.memorio.memorio.services;

import com.memorio.memorio.entities.GameHistory;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameHistoryService {
    private GameRepository gameRepository;

    @Autowired
    public GameHistoryService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameHistory getGameHistoryForUser(User user) {
        return new GameHistory(0, 0, 0, 0);
    }
}