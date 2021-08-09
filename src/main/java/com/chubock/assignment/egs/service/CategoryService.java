package com.chubock.assignment.egs.service;

import com.chubock.assignment.egs.entity.Category;
import com.chubock.assignment.egs.mapper.AbstractMapper;
import com.chubock.assignment.egs.mapper.CategoryMapper;
import com.chubock.assignment.egs.model.CategoryModel;
import com.chubock.assignment.egs.repository.AbstractRepository;
import com.chubock.assignment.egs.repository.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryService extends AbstractService<Category, CategoryModel> {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryModel save(CategoryModel model) {

        if (StringUtils.isBlank(model.getName()))
            throw new IllegalArgumentException("error.category.name.empty");

        boolean duplicateName = categoryRepository.findByName(model.getName())
                .filter(category -> !category.getUid().equals(model.getUid()))
                .isPresent();

        if (duplicateName)
            throw new IllegalArgumentException("error.category.name.duplicate");

        Category category = Optional.ofNullable(model.getUid())
                .flatMap(categoryRepository::findByUid)
                .orElseGet(Category::new);

        if (model.getUid() != null)
            category.setUid(model.getUid());

        category.setName(model.getName());

        categoryRepository.save(category);

        return findOneMapper(category);

    }

    @Override
    AbstractRepository<Category> getRepository() {
        return categoryRepository;
    }

    @Override
    AbstractMapper<Category, CategoryModel> getMapper() {
        return categoryMapper;
    }

}
