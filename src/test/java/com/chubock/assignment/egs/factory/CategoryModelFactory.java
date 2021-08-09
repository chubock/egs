package com.chubock.assignment.egs.factory;

import com.chubock.assignment.egs.model.CategoryModel;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

public class CategoryModelFactory {

    public CategoryModel newModel() {
        return CategoryModel.builder()
                .name(RandomStringUtils.randomAlphabetic(5))
                .build();
    }

}
