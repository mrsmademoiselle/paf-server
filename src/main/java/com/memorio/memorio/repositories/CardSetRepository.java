package com.memorio.memorio.repositories;

import com.memorio.memorio.entities.CardSet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardSetRepository extends CrudRepository<CardSet, Long> {

}