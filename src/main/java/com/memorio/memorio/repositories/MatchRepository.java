package com.memorio.memorio.repositories;

import com.memorio.memorio.entities.Match;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match, String> {
}