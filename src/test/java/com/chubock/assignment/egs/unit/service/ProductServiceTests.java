package com.chubock.assignment.egs.unit.service;

import com.chubock.assignment.egs.entity.Category;
import com.chubock.assignment.egs.entity.Product;
import com.chubock.assignment.egs.mapper.ProductMapper;
import com.chubock.assignment.egs.repository.CategoryRepository;
import com.chubock.assignment.egs.repository.ProductRepository;
import com.chubock.assignment.egs.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class ProductServiceTests {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    private ProductService service;

    @BeforeEach
    void init() {

        productRepository = Mockito.mock(ProductRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);

        ProductMapper mapper = Mockito.mock(ProductMapper.class);

        service = new ProductService(productRepository, categoryRepository, mapper);

    }

    @Test
    public void findAllByCategoryShouldThrowIllegalArgumentExceptionWhenCategoryUidIsNull() {

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.findAllByCategory(null, PageRequest.ofSize(20)));

    }

    @Test
    public void findAllByCategoryShouldThrowIllegalArgumentExceptionWhenCategoryUidIsEmpty() {

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.findAllByCategory("", PageRequest.ofSize(20)));

    }


    @Test
    public void findAllByCategoryShouldThrowIllegalArgumentExceptionWhenCategoryNotFound() {

        Mockito.when(categoryRepository.findByUid("foo"))
                .thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.findAllByCategory("foo", PageRequest.ofSize(20)));

    }

    @Test
    public void changeProductCategoryShouldThrowIllegalArgumentExceptionWhenProductUidIsNull() {

        Mockito.when(categoryRepository.findByUid("bar"))
                .thenReturn(Optional.of(Category.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.changeProductCategory(null, "bar"));

    }

    @Test
    public void changeProductCategoryShouldThrowIllegalArgumentExceptionWhenProductUidIsEmpty() {

        Mockito.when(categoryRepository.findByUid("bar"))
                .thenReturn(Optional.of(Category.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.changeProductCategory("", "bar"));

    }

    @Test
    public void changeProductCategoryShouldThrowIllegalArgumentExceptionWhenProductNotFound() {

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.empty());

        Mockito.when(categoryRepository.findByUid("bar"))
                .thenReturn(Optional.of(Category.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.changeProductCategory("foo", "bar"));

    }

    @Test
    public void changeProductCategoryShouldThrowIllegalArgumentExceptionWhenCategoryUidIsNull() {

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.changeProductCategory("foo", null));

    }

    @Test
    public void changeProductCategoryShouldThrowIllegalArgumentExceptionWhenCategoryUidIsEmpty() {

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.changeProductCategory("foo", ""));

    }

    @Test
    public void changeProductCategoryShouldThrowIllegalArgumentExceptionWhenCategoryNotFound() {

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        Mockito.when(categoryRepository.findByUid("bar"))
                .thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.changeProductCategory("foo", "bar"));

    }

    @Test
    public void removeFromCategoryShouldThrowIllegalArgumentExceptionWhenProductUidIsNull() {

        Mockito.when(categoryRepository.findByUid("bar"))
                .thenReturn(Optional.of(Category.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.removeFromCategory(null, "bar"));

    }

    @Test
    public void removeFromCategoryShouldThrowIllegalArgumentExceptionWhenProductUidIsEmpty() {

        Mockito.when(categoryRepository.findByUid("bar"))
                .thenReturn(Optional.of(Category.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.removeFromCategory("", "bar"));

    }

    @Test
    public void removeFromCategoryShouldThrowIllegalArgumentExceptionWhenProductNotFound() {

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.empty());

        Mockito.when(categoryRepository.findByUid("bar"))
                .thenReturn(Optional.of(Category.builder().uid("bar").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.removeFromCategory("foo", "bar"));

    }

    @Test
    public void removeFromCategoryShouldThrowIllegalArgumentExceptionWhenCategoryUidIsNull() {

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.removeFromCategory("foo", null));

    }

    @Test
    public void removeFromCategoryShouldThrowIllegalArgumentExceptionWhenCategoryUidIsEmpty() {

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.removeFromCategory("foo", ""));

    }

    @Test
    public void removeFromCategoryShouldThrowIllegalArgumentExceptionWhenCategoryNotFound() {

        Mockito.when(productRepository.findByUid("foo"))
                .thenReturn(Optional.of(Product.builder().uid("foo").build()));

        Mockito.when(categoryRepository.findByUid("bar"))
                .thenReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.removeFromCategory("foo", "bar"));

    }

}
