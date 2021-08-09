package com.chubock.assignment.egs.mapper;


import com.chubock.assignment.egs.entity.AbstractEntity;
import com.chubock.assignment.egs.model.AbstractModel;

public interface AbstractMapper<T extends AbstractEntity, R extends AbstractModel<T>> {

    T toEntity(R model);
    R toModel(T entity);

}
