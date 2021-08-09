package com.chubock.assignment.egs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface AbstractRepository<T> extends JpaRepository<T, Long> {

    Optional<T> findByUid(String uid);
    void deleteByUid(String uid);

}
