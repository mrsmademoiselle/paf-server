package com.memorio.memorio.repositories;

import com.memorio.memorio.entities.CardSet;
import com.memorio.memorio.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardSetRepository extends CrudRepository<CardSet, Long> {
    Optional<User> findById(long cardSetId);

    Optional<User> findByName(String cardSetName);

}