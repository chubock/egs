package com.chubock.assignment.egs.service;

import com.chubock.assignment.egs.model.AbstractModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BaseService<T extends AbstractModel<?>> {

    Page<T> findAll(Pageable pageable);
    Optional<T> findOne(String uid);
    T save(T model);
    void delete(String uid);

}
