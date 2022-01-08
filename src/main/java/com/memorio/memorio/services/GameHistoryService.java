package com.memorio.memorio.services;

import com.memorio.memorio.entities.Game;
import com.memorio.memorio.entities.GameHistory;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.entities.UserScore;
import com.memorio.memorio.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class GameHistoryService {
    private GameRepository gameRepository;

    @Autowired
    public GameHistoryService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameHistory getGameHistoryForUser(User user) {
        List<Game> gamesOfUser = gameRepository.findByUserScoresUser(user);

        int winCount = calculateWinCount(gamesOfUser, user);
        int averageMoves = calculateAverageMoves(gamesOfUser, user);
        int losses = gamesOfUser.size() - winCount;

        return new GameHistory(gamesOfUser.size(), winCount, losses, averageMoves);
    }

    private int calculateWinCount(List<Game> allGames, User user) {
        int totalWins = 0;

        for (Game game : allGames) {
            List<UserScore> userScores = game.getUserScores();
            UserScore highestUserScore = userScores.stream()
                    // hole mir das größte Element anhand dieses Comparators
                    .max(Comparator.comparingInt(UserScore::getMoves))
                    .orElse(userScores.get(0));

            if (user.getUsername().equals(highestUserScore.getUser().getUsername())) totalWins++;
        }

        return totalWins;
    }

    private int calculateAverageMoves(List<Game> allGames, User user) {
        int totalGames = allGames.size();
        Integer totalSumOfPoints = allGames.stream()
                // wir holen uns die UserScores
                .flatMap(game -> game.getUserScores().stream())
                // wir holen uns davon nur die von unserem User
                .filter(u -> u.getUser().getUsername().equals(user.getUsername()))
                // wir mappen sie auf seine Moves
                .map(UserScore::getMoves)
                // und addieren alle aufeinander
                .reduce(0, Integer::sum);

        return totalSumOfPoints / totalGames;
    }
}