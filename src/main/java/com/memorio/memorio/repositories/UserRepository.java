package com.memorio.memorio.repositories;

import com.memorio.memorio.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    Optional<User> findById(long userId);

    Optional<User> findByUsername(String username);

    boolean existsById(long userId);

    boolean existsByUsername(String username);

    boolean existsByPassword(String password);
}