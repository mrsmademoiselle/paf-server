package com.memorio.memorio.services;

import com.memorio.memorio.entities.Game;
import com.memorio.memorio.entities.UserScore;
import com.memorio.memorio.repositories.GameRepository;
import com.memorio.memorio.web.dto.GameHistoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Service
public class GameHistoryService {
    private GameRepository gameRepository;

    @Autowired
    public GameHistoryService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Baut für diesen User eine GameHistoryDto zusammen und gibt sie zurück.
     */
    public GameHistoryDto getGameHistoryForUser(String username) {
        List<Game> gamesOfUser = gameRepository.findByUserScoresUserUsername(username);

        if (gamesOfUser.size() == 0) return createGameHistoryWithRandomValues();

        int winCount = calculateWinCount(gamesOfUser, username);
        int averageMoves = calculateAverageMoves(gamesOfUser, username);

        return new GameHistoryDto(gamesOfUser.size(), winCount, averageMoves);
    }

    private GameHistoryDto createGameHistoryWithRandomValues() {
        Random random = new Random();
        int maxGames = random.nextInt(20);
        int averageMoves = random.nextInt(40);
        int winCount = random.nextInt(maxGames);

        return new GameHistoryDto(maxGames, winCount, averageMoves);
    }

    private int calculateWinCount(List<Game> allGames, String username) {
        int totalWins = 0;

        for (Game game : allGames) {
            List<UserScore> userScores = game.getUserScores();
            UserScore highestUserScore = userScores.stream()
                    // hole mir das größte Element anhand dieses Comparators
                    .max(Comparator.comparingInt(UserScore::getMoves))
                    .orElse(userScores.get(0));

            // Wenn unser User den höchsten Spielwert hat, erhöhe den Counter
            if (username.equals(highestUserScore.getUser().getUsername())) totalWins++;
        }

        return totalWins;
    }

    private int calculateAverageMoves(List<Game> allGames, String username) {
        int totalGames = allGames.size();
        Integer totalSumOfPoints = allGames.stream()
                // wir holen uns die UserScores
                .flatMap(game -> game.getUserScores().stream())
                // wir holen uns davon nur die von unserem User
                .filter(u -> u.getUser().getUsername().equals(username))
                // wir mappen sie auf seine Moves
                .map(UserScore::getMoves)
                // und addieren alle aufeinander
                .reduce(0, Integer::sum);

        return totalSumOfPoints / totalGames;
    }
}