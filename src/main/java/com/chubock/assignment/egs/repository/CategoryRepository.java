package com.chubock.assignment.egs.repository;

import com.chubock.assignment.egs.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends AbstractRepository<Category> {

    Optional<Category> findByName(String name);

}
