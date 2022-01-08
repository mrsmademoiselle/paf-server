package com.memorio.memorio;

import com.memorio.memorio.entities.Board;
import com.memorio.memorio.entities.Game;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.repositories.GameRepository;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.websocket.MemorioWebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
@SpringBootApplication
public class MemorioApplication implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GameRepository gameRepository;

    public static void main(String[] args) {
        SpringApplication.run(MemorioApplication.class, args);

        // starte den WebSocketServer in einem neuen Thread.
        // TODO den WebSocketServer korrekt herunterfahren mit server.close() oder so...
        MemorioWebSocketServer server = MemorioWebSocketServer.getInstance();
        new Thread(() -> {
            server.start();
        }).start();
        System.out.println("Startup successful");
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User("usi", "usi");
        User user2 = new User("busi", "busi");
        User user3 = new User("kusi", "kusi");
        User user4 = new User("lusi", "lusi");

        userRepository.saveAll(Arrays.asList(user1, user2, user3, user4));
        System.out.println("Registered basic users");

        Game game1 = new Game(new Board(), user1, user1, user2);
        game1.getUserScores().get(0).increaseScoreByRandomAmount();
        game1.getUserScores().get(1).increaseScoreByRandomAmount();
        Game game2 = new Game(new Board(), user2, user2, user3);
        game2.getUserScores().get(0).increaseScoreByRandomAmount();
        game2.getUserScores().get(1).increaseScoreByRandomAmount();
        Game game3 = new Game(new Board(), user2, user2, user4);
        Game game4 = new Game(new Board(), user4, user4, user2);
        game4.getUserScores().get(0).increaseScoreByRandomAmount();
        game4.getUserScores().get(1).increaseScoreByRandomAmount();

        gameRepository.saveAll(Arrays.asList(game1, game2, game3, game4));
        System.out.println("Registered games");
    }

    /* Exponiert alles was in /public/ drin ist nach aussen
     * Es stellt eine Alternative zum Endpunkt da, ueber dem Bilder abgefragt werden
     * und hat den Vorteil das Ressourcen direkt im fxml/jsx referenziert werden koennen */
    @Configuration
    @EnableWebMvc
    public class MvcConfig implements WebMvcConfigurer {
        // Uebeshreiben des Standartressourcehandlers: Standartmaessig werden noch andere Verzeichnisse nach aussen exponiert
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // Mappen von allem was die /public/** URI beeinhaltet auf die entsprechende Ressource im /public/ Verzeichnis
            registry
                    .addResourceHandler("/public/**")
                    .addResourceLocations("classpath:/public/");
        }
    }

}