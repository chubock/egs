package com.chubock.assignment.egs.unit.service;

import com.chubock.assignment.egs.entity.Category;
import com.chubock.assignment.egs.mapper.CategoryMapper;
import com.chubock.assignment.egs.model.CategoryModel;
import com.chubock.assignment.egs.repository.CategoryRepository;
import com.chubock.assignment.egs.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class CategoryServiceTests {

    private CategoryRepository repository;

    private CategoryService categoryService;

    @BeforeEach
    void init() {

        repository = Mockito.mock(CategoryRepository.class);
        CategoryMapper mapper = Mockito.mock(CategoryMapper.class);

        categoryService = new CategoryService(repository, mapper);

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenNameIsNull() {

        assertThatIllegalArgumentException()
                .isThrownBy(() -> categoryService.save(new CategoryModel()));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenNameIsEmpty() {

        assertThatIllegalArgumentException()
                .isThrownBy(() -> categoryService.save(CategoryModel.builder().name("").build()));

    }

    @Test
    public void saveShouldThrowIllegalArgumentExceptionWhenNameIsDuplicate() {

        Mockito.when(repository.findByName("foo"))
                .thenReturn(Optional.of(Category.builder().name("foo").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> categoryService.save(CategoryModel.builder().name("foo").build()));

    }

}
