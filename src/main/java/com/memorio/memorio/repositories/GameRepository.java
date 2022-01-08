package com.memorio.memorio.repositories;

import com.memorio.memorio.entities.Game;
import com.memorio.memorio.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    List<Game> findAll();

    /**
     * holt alle Games, in denen ein UserScore vom User mit diesem Namen vorhanden ist
     */
    List<Game> findByUserScoresUserUsername(String username);

    List<Game> findByUserScoresUser(User user);
}