package com.chubock.assignment.egs.mapper;

import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.model.ProductModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper extends AbstractMapper<Product, ProductModel> {

    @Override
    @Mapping(target = "category", ignore = true)
    ProductModel toModel(Product entity);

    ProductModel toModelWithCategory(Product entity);

}
