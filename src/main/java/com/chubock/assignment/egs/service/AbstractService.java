package com.chubock.assignment.egs.service;


import com.chubock.assignment.egs.entity.AbstractEntity;
import com.chubock.assignment.egs.mapper.AbstractMapper;
import com.chubock.assignment.egs.model.AbstractModel;
import com.chubock.assignment.egs.repository.AbstractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public abstract class AbstractService<T extends AbstractEntity, R extends AbstractModel<T>> implements BaseService<R> {

    abstract AbstractRepository<T> getRepository();
    abstract AbstractMapper<T, R> getMapper();

    @Override
    @Transactional(readOnly = true)
    public Page<R> findAll(Pageable pageable) {
        return getRepository().findAll(pageable)
                .map(this::findAllMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<R> findOne(String uid) {
        return getRepository().findByUid(uid)
                .map(this::findOneMapper);
    }

    @Override
    @Transactional
    public void delete(String uid) {
        getRepository().deleteByUid(uid);
    }

    R findAllMapper(T entity) {
        return getMapper().toModel(entity);
    }

    R findOneMapper(T entity) {
        return getMapper().toModel(entity);
    }
}
