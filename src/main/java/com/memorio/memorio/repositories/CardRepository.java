package com.memorio.memorio.repositories;

import com.memorio.memorio.entities.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<Card, Long> {
    @Override
    List<Card> findAll();
}