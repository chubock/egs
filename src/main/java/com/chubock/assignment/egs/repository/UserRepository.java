package com.chubock.assignment.egs.repository;

import com.chubock.assignment.egs.entity.User;

import java.util.Optional;

public interface UserRepository extends AbstractRepository<User> {

    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

}
