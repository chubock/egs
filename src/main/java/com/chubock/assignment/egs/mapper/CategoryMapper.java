package com.chubock.assignment.egs.mapper;

import com.chubock.assignment.egs.entity.Category;
import com.chubock.assignment.egs.model.CategoryModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends AbstractMapper<Category, CategoryModel> {
    @Override
    Category toEntity(CategoryModel model);

    @Override
    CategoryModel toModel(Category entity);
}
