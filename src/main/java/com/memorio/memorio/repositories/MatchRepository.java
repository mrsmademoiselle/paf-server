package com.memorio.memorio.repositories;

import com.memorio.memorio.entities.Match;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends CrudRepository<Match, Long> {
    List<Match> findAll();
}