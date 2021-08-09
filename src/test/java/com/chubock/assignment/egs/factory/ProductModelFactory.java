package com.chubock.assignment.egs.factory;

import com.chubock.assignment.egs.model.CategoryModel;
import com.chubock.assignment.egs.model.ProductModel;
import org.apache.commons.lang3.RandomUtils;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

public class ProductModelFactory {

    public ProductModel newModel() {
        return newModel(null);
    }

    public ProductModel newModel(CategoryModel categoryModel) {
        return ProductModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .price(RandomUtils.nextInt(1000, 10000))
                .category(categoryModel)
                .build();
    }

}
